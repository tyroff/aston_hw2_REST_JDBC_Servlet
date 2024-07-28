package controller;

import com.google.gson.Gson;
import dto.PatientDTO;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import service.PatientService;

import java.io.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServletTest {
    @Mock
    private PatientService patientService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private PatientServlet patientServlet;


    private final Gson gson = new Gson();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void initTest() throws ServletException {
        ServletConfig servletConfig = Mockito.mock(ServletConfig.class);
        patientServlet.init(servletConfig);
        assertNotNull(patientServlet);
    }

    @Test
    public void doGetByIdTest() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(printWriter);

        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(1L);
        patientDTO.setLastName("Lastname doGetUserByIdTest");
        patientDTO.setFirstName("Firstname doGetUserByIdTest");
        patientDTO.setPatronymic("Patronymic doGetUserByIdTest");
        patientDTO.setJob("Job doGetUserByIdTest");

        when(patientService.getById(1L)).thenReturn(patientDTO);

        patientServlet.doGet(request, response);

        verify(patientService, times(1)).getById(1L);

        String expectedJson = gson.toJson(patientDTO);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    public void doGetAllTest() throws ServletException, IOException, SQLException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(request.getPathInfo()).thenReturn(null);
        when(response.getWriter()).thenReturn(printWriter);

        PatientDTO patientDTO1 = new PatientDTO();
        patientDTO1.setId(1L);
        patientDTO1.setLastName("Lastname doGetUserByIdTest");
        patientDTO1.setFirstName("Firstname doGetUserByIdTest");
        patientDTO1.setPatronymic("Patronymic doGetUserByIdTest");
        patientDTO1.setJob("Job doGetUserByIdTest");

        PatientDTO patientDTO2 = new PatientDTO();
        patientDTO2.setId(2L);
        patientDTO2.setLastName("Lastname doGetUserByIdTest");
        patientDTO2.setFirstName("Firstname doGetUserByIdTest");
        patientDTO2.setPatronymic("Patronymic doGetUserByIdTest");
        patientDTO2.setJob("Job doGetUserByIdTest");

        List<PatientDTO> patientDTOs = Arrays.asList(patientDTO1, patientDTO2);

        when(patientService.getAll()).thenReturn(patientDTOs);

        patientServlet.doGet(request, response);

        verify(patientService, times(1)).getAll();

        String expectedJson = gson.toJson(patientDTOs);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    public void doPostTest() throws IOException, ServletException, SQLException {
        String patientJson = "{\"lastName\":\"I am\",\"firstName\":\"The\",\"patronymic\":\"Batman\",\"job\":\"Defend\"}";

        BufferedReader reader = new BufferedReader(new StringReader(patientJson));
        when(request.getReader()).thenReturn(reader);

        patientServlet.doPost(request, response);

        verify(patientService, times(1)).save(any(PatientDTO.class));
        verify(response, times(1)).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    public void doPutTest() throws ServletException, IOException, SQLException {
        String patientJson = "{\"lastName\":\"I am\",\"firstName\":\"The\",\"patronymic\":\"Batman\",\"job\":\"Defend\"}";

        BufferedReader reader = new BufferedReader(new StringReader(patientJson));
        when(request.getReader()).thenReturn(reader);
        when(request.getPathInfo()).thenReturn("/1");

        patientServlet.doPut(request, response);

        verify(patientService, times(1)).update(any(PatientDTO.class));
        verify(response, times(1)).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    public void doDeleteTest() throws ServletException, IOException, SQLException {
        when(request.getPathInfo()).thenReturn("/1");

        patientServlet.doDelete(request, response);

        verify(patientService, times(1)).deleteById(1L);
    }
}