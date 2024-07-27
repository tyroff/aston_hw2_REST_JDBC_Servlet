package controller;

import com.google.gson.Gson;
import dto.DoctorDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.DoctorService;
import util.DataPropertiesUtil;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

/**
 * This class receives data from the server, sends requests to the server and sends the result back to the server.
 */
@WebServlet("/doctors/*")
public class DoctorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DoctorService doctorService;
    private final Gson gson = new Gson();

    /**
     * The method is necessary to initialize this server and obtain a dependency on the server.
     */
    @Override
    public void init() {
        DataSource source = DataPropertiesUtil.getDataSource();
        this.doctorService = new DoctorService(source);
    }

    /**
     * The method is necessary to obtain data on the ID of one Doctor or all.
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
                String doctorJson = gson.toJson(doctorService.getAll());
                resp.getWriter().write(doctorJson);
            } else {
                Long id = Long.parseLong(pathInfo.split("/")[1]);
                DoctorDTO doctorDTO = doctorService.getById(id);
                String doctorJson = gson.toJson(doctorDTO);
                resp.getWriter().write(doctorJson);
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid patient ID format.");
        }
    }

    /**
     * This method receives data from the Doctor entity and transfers it to the service for saving in the database.
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
        DoctorDTO doctorDTO = gson.fromJson(stringBuilder.toString(), DoctorDTO.class);
        try {
            if (doctorDTO.getLastName() == null || doctorDTO.getLastName().isEmpty()
                    || doctorDTO.getFirstName() == null || doctorDTO.getFirstName().isEmpty()
                    || doctorDTO.getPatronymic() == null || doctorDTO.getPatronymic().isEmpty()
                    || doctorDTO.getSpecialization() == null || doctorDTO.getSpecialization().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "All fields must be filled in.");
                return;
            }
            doctorService.save(doctorDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    /**
     * This method receives data from the Doctor entity and transfers it to the service for updating in the database.
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
            DoctorDTO doctorDTO = gson.fromJson(sb.toString(), DoctorDTO.class);
            doctorDTO.setId(id);
            if (doctorDTO.getLastName() == null || doctorDTO.getLastName().isEmpty()
                    || doctorDTO.getFirstName() == null || doctorDTO.getFirstName().isEmpty()
                    || doctorDTO.getPatronymic() == null || doctorDTO.getPatronymic().isEmpty()
                    || doctorDTO.getSpecialization() == null || doctorDTO.getSpecialization().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "All fields must be filled in.");
                return;
            }
            doctorService.update(doctorDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException e) {
            throw new ServletException(e);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid patient ID.");
        }
    }

    /**
     * This method receives the ID of the Doctor entity from the server and passes it to the service to remove it from the database.
     * @param req request.
     * @param resp response.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Need patient ID for deleting.");
            } else {
                Long id = Long.parseLong(pathInfo.split("/")[1]);
                if (doctorService.deleteById(id)) {
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
