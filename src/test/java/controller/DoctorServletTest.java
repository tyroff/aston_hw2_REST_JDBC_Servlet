package controller;

import com.google.gson.Gson;
import dto.DoctorDTO;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.DoctorService;
import org.mockito.InjectMocks;

import java.io.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class DoctorServletTest {
    @Mock
    private DoctorService doctorService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private DoctorServlet doctorServlet;

    private final Gson gson = new Gson();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void initTest() throws ServletException {
        ServletConfig servletConfig = Mockito.mock(ServletConfig.class);
        doctorServlet.init(servletConfig);
        assertNotNull(doctorServlet);
    }

    @Test
    public void doGetByIdTest() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(printWriter);

        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(1L);
        doctorDTO.setLastName("Lastname doGetUserByIdTest");
        doctorDTO.setFirstName("Firstname doGetUserByIdTest");
        doctorDTO.setPatronymic("Patronymic doGetUserByIdTest");
        doctorDTO.setSpecialization("Specialization doGetUserByIdTest");

        when(doctorService.getById(1L)).thenReturn(doctorDTO);

        doctorServlet.doGet(request, response);

        verify(doctorService, times(1)).getById(1L);

        String expectedJson = gson.toJson(doctorDTO);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    public void doGetAllTest() throws ServletException, IOException, SQLException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(request.getPathInfo()).thenReturn(null);
        when(response.getWriter()).thenReturn(printWriter);

        DoctorDTO doctorDTO1 = new DoctorDTO();
        doctorDTO1.setId(1L);
        doctorDTO1.setLastName("Lastname doGetAllTest");
        doctorDTO1.setFirstName("Firstname doGetAllTest");
        doctorDTO1.setPatronymic("Patronymic doGetAllTest");
        doctorDTO1.setSpecialization("Specialization doGetAllTest");

        DoctorDTO doctorDTO2 = new DoctorDTO();
        doctorDTO2.setId(2L);
        doctorDTO2.setLastName("Lastname doGetAllTest");
        doctorDTO2.setFirstName("Firstname doGetAllTest");
        doctorDTO2.setPatronymic("Patronymic doGetAllTest");
        doctorDTO2.setSpecialization("Specialization doGetAllTest");

        List<DoctorDTO> doctorDTOs = Arrays.asList(doctorDTO1, doctorDTO2);

        when(doctorService.getAll()).thenReturn(doctorDTOs);

        doctorServlet.doGet(request, response);

        verify(doctorService, times(1)).getAll();

        String expectedJson = gson.toJson(doctorDTOs);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    public void doPostTest() throws IOException, ServletException, SQLException {
        String doctorJson = "{\"lastName\":\"I am\",\"firstName\":\"The\",\"patronymic\":\"Badman\",\"specialization\":\"Kill\"}";

        BufferedReader reader = new BufferedReader(new StringReader(doctorJson));
        when(request.getReader()).thenReturn(reader);

        doctorServlet.doPost(request, response);

        verify(doctorService, times(1)).save(any(DoctorDTO.class));
        verify(response, times(1)).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    public void doPutTest() throws ServletException, IOException, SQLException {
        String doctorJson = "{\"lastName\":\"I am\",\"firstName\":\"The\",\"patronymic\":\"Badman\",\"specialization\":\"Kill\"}";

        BufferedReader reader = new BufferedReader(new StringReader(doctorJson));
        when(request.getReader()).thenReturn(reader);
        when(request.getPathInfo()).thenReturn("/1");

        doctorServlet.doPut(request, response);

        verify(doctorService, times(1)).update(any(DoctorDTO.class));
        verify(response, times(1)).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    public void doDeleteTest() throws ServletException, IOException, SQLException {
        when(request.getPathInfo()).thenReturn("/1");

        doctorServlet.doDelete(request, response);

        verify(doctorService, times(1)).deleteById(1L);
    }
}
