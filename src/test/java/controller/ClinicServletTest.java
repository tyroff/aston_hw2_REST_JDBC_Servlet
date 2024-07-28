package controller;

import com.google.gson.Gson;
import dto.ClinicDTO;
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
import service.ClinicService;

import java.io.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ClinicServletTest {
    @Mock
    private ClinicService clinicService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private ClinicServlet clinicServlet;

    private final Gson gson = new Gson();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void initTest() throws ServletException {
        ServletConfig servletConfig = Mockito.mock(ServletConfig.class);
        clinicServlet.init(servletConfig);
        assertNotNull(clinicServlet);
    }

    @Test
    public void doGetByIdTest() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(printWriter);

        ClinicDTO clinicDTO = new ClinicDTO();
        clinicDTO.setId(1L);
        clinicDTO.setName("Name doGetByIdTest");
        clinicDTO.setAddress("Address doGetByIdTest");
        clinicDTO.setType("Type doGetUserByIdTest");

        when(clinicService.getById(1L)).thenReturn(clinicDTO);

        clinicServlet.doGet(request, response);

        verify(clinicService, times(1)).getById(1L);

        String expectedJson = gson.toJson(clinicDTO);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    public void doGetAllTest() throws ServletException, IOException, SQLException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(request.getPathInfo()).thenReturn(null);
        when(response.getWriter()).thenReturn(printWriter);

        ClinicDTO clinicDTO1 = new ClinicDTO();
        clinicDTO1.setId(1L);
        clinicDTO1.setName("Name doGetByIdTest");
        clinicDTO1.setAddress("Address doGetByIdTest");
        clinicDTO1.setType("Type doGetUserByIdTest");

        ClinicDTO clinicDTO2 = new ClinicDTO();
        clinicDTO2.setId(2L);
        clinicDTO2.setName("Name doGetByIdTest");
        clinicDTO2.setAddress("Address doGetByIdTest");
        clinicDTO2.setType("Type doGetUserByIdTest");

        List<ClinicDTO> clinicDTOS = Arrays.asList(clinicDTO1, clinicDTO1);

        when(clinicService.getAll()).thenReturn(clinicDTOS);

        clinicServlet.doGet(request, response);

        verify(clinicService, times(1)).getAll();

        String expectedJson = gson.toJson(clinicDTOS);
        assertEquals(expectedJson, stringWriter.toString());
    }


    /*
name_clinic, address, type_clinic
    "name": "Клиника №666",
    "address": "Адрес №777",
    "type": "Дурдом",
     */
    @Test
    public void doPostTest() throws IOException, ServletException, SQLException {
        String clinicJson = "{\"name\":\"Clinic\",\"address\":\"There\",\"type\":\"What\"}";

        BufferedReader reader = new BufferedReader(new StringReader(clinicJson));
        when(request.getReader()).thenReturn(reader);

        clinicServlet.doPost(request, response);

        verify(clinicService, times(1)).save(any(ClinicDTO.class));
        verify(response, times(1)).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    public void doPutTest() throws ServletException, IOException, SQLException {
        String clinicJson = "{\"name\":\"Clinic\",\"address\":\"There\",\"type\":\"What\"}";

        BufferedReader reader = new BufferedReader(new StringReader(clinicJson));
        when(request.getReader()).thenReturn(reader);
        when(request.getPathInfo()).thenReturn("/1");

        clinicServlet.doPut(request, response);

        verify(clinicService, times(1)).update(any(ClinicDTO.class));
        verify(response, times(1)).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    public void doDeleteTest() throws IOException {
        when(request.getPathInfo()).thenReturn("/1");

        clinicServlet.doDelete(request, response);

        verify(clinicService, times(1)).deleteById(1L);
    }
}
