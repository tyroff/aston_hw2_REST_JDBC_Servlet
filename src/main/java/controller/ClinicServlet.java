package controller;

import com.google.gson.Gson;
import dao.ClinicDaoImp;
import dao.DoctorDaoImp;
import dao.PatientDaoImp;
import dto.ClinicDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ClinicService;
import util.DataPropertiesUtil;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

/**
 * This class receives data from the server, sends requests to the server and sends the result back to the server.
 */
@WebServlet("/clinics/*")
public class ClinicServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ClinicService clinicService;
    private final Gson gson = new Gson();

    /**
     * The method is necessary to initialize this server and obtain a dependency on the server.
     */
    @Override
    public void init() {
        DataSource source = DataPropertiesUtil.getDataSource();
        this.clinicService = new ClinicService(new ClinicDaoImp(source, new DoctorDaoImp(source), new PatientDaoImp(source)), new DoctorDaoImp(source), new PatientDaoImp(source));
    }

    /**
     * The method is necessary to obtain data on the ID of one Clinic or all.
     * @param req request.
     * @param resp response.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        try {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            if (pathInfo == null || pathInfo.equals("/")) {
                String clinicJson = gson.toJson(clinicService.getAll());
                resp.getWriter().write(clinicJson);
            } else {
                Long id = Long.parseLong(pathInfo.split("/")[1]);
                ClinicDTO clinicDTO = clinicService.getById(id);
                String clinicJson = gson.toJson(clinicDTO);
                resp.getWriter().write(clinicJson);
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid patient ID format.");
        }
    }

    /**
     * This method receives data from the Clinic entity and transfers it to the service for saving in the database.
     * @param req request.
     * @param resp response.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        ClinicDTO clinicDTO = gson.fromJson(stringBuilder.toString(), ClinicDTO.class);
        try {
            if (clinicDTO.getName() == null || clinicDTO.getName().isEmpty()
                    || clinicDTO.getAddress() == null || clinicDTO.getAddress().isEmpty()
                    || clinicDTO.getType() == null || clinicDTO.getType().isEmpty()
                    || clinicDTO.getDoctors() == null || clinicDTO.getDoctors().isEmpty()
                    || clinicDTO.getPatients() == null || clinicDTO.getPatients().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "All fields must be filled in.");
                return;
            }
            clinicService.save(clinicDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    /**
     * This method receives data from the Clinic entity and transfers it to the service for updating in the database.
     * @param req request.
     * @param resp response.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        try {
            Long id = Long.parseLong(pathInfo.split("/")[1]);
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            ClinicDTO clinicDTO = gson.fromJson(sb.toString(), ClinicDTO.class);
            clinicDTO.setId(id);
            if (clinicDTO.getName() == null || clinicDTO.getName().isEmpty()
                    || clinicDTO.getAddress() == null || clinicDTO.getAddress().isEmpty()
                    || clinicDTO.getType() == null || clinicDTO.getType().isEmpty()
                    || clinicDTO.getDoctors() == null || clinicDTO.getDoctors().isEmpty()
                    || clinicDTO.getPatients() == null || clinicDTO.getPatients().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "All fields must be filled in.");
                return;
            }
            clinicService.update(clinicDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException e) {
            throw new ServletException(e);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid patient ID.");
        }
    }

    /**
     * This method receives the ID of the Clinic entity from the server and passes it to the service to remove it from the database.
     * @param req request.
     * @param resp response.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Need clinic ID for deleting.");
            } else {
                Long id = Long.parseLong(pathInfo.split("/")[1]);
                if (clinicService.deleteById(id)) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid patient ID.");
        }
    }
}
