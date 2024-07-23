package controller;

import com.google.gson.Gson;
import dao.PatientDaoImp;
import dto.PatientDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.PatientService;
import util.DataPropertiesUtil;

import java.io.IOException;

@WebServlet("/patients/*")
public class PatientServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PatientService patientService;
    private final Gson gson = new Gson();
    @Override
    public void init() {
        this.patientService = new PatientService(new PatientDaoImp(DataPropertiesUtil.getDataSource()));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        try {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            if (pathInfo == null || pathInfo.equals("/")) {
                //TODO: allPatient;
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
