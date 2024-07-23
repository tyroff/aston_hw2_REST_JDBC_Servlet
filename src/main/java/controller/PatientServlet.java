package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.PatientDaoImp;
import dto.PatientDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.PatientService;
import util.DataPropertiesUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class receives data from the server, sends requests to the server and sends the result back to the server.
 */
@WebServlet("/patients/*")
public class PatientServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PatientService patientService;
    private final Gson gson = new Gson();
    private final GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("MM/dd/yyyy");

    /**
     * The method is necessary to initialize this server and obtain a dependency on the server.
     */
    @Override
    public void init() {
        this.patientService = new PatientService(new PatientDaoImp(DataPropertiesUtil.getDataSource()));
    }

    /**
     * The method is necessary to obtain data on the ID of one Patient or all.
     * @param req request.
     * @param resp response.
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        try {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            if (pathInfo == null || pathInfo.equals("/")) {
                String patientJson = gson.toJson(patientService.getAll());
                resp.getWriter().write(patientJson);
            } else {
                Long id = Long.parseLong(pathInfo.split("/")[1]);
                PatientDTO patientDTO = patientService.getById(id);
                String patientJson = gson.toJson(patientDTO);
                resp.getWriter().write(patientJson);
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid patient ID format.");
        }
    }

    /**
     * This method receives data from the Patient entity and transfers it to the service for saving in the database.
     * @param req request.
     * @param resp response.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        PatientDTO patientDTO = gson.fromJson(stringBuilder.toString(), PatientDTO.class);
        try {
            if (patientDTO.getLastName() == null || patientDTO.getLastName().isEmpty()
                    || patientDTO.getFirstName() == null || patientDTO.getFirstName().isEmpty()
                    || patientDTO.getPatronymic() == null || patientDTO.getPatronymic().isEmpty()
                    || patientDTO.getJob() == null || patientDTO.getJob().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "All fields must be filled in.");
                return;
            }
            patientService.save(patientDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    /**
     * This method receives data from the Patient entity and transfers it to the service for updating in the database.
     * @param req request.
     * @param resp response.
     * @throws ServletException
     * @throws IOException
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
            PatientDTO patientDTO = gson.fromJson(sb.toString(), PatientDTO.class);
            patientDTO.setId(id);
            if (patientDTO.getLastName() == null || patientDTO.getLastName().isEmpty()
                    || patientDTO.getFirstName() == null || patientDTO.getFirstName().isEmpty()
                    || patientDTO.getPatronymic() == null || patientDTO.getPatronymic().isEmpty()
                    || patientDTO.getJob() == null || patientDTO.getJob().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "All fields must be filled in.");
                return;
            }
            patientService.update(patientDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException e) {
            throw new ServletException(e);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid patient ID.");
        }
    }

    /**
     * This method receives the ID of the Patient entity from the server and passes it to the service to remove it from the database.
     * @param req request.
     * @param resp response.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Need patient ID for deleting.");
            } else {
                Long id = Long.parseLong(pathInfo.split("/")[1]);
                if (patientService.deleteById(id)) {
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
