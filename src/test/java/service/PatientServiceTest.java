package service;

import dao.PatientDaoImp;
import dto.PatientDTO;
import model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.*;

class PatientServiceTest {
    private PatientService patientService;
    private PatientDaoImp patientDaoImp;


    @BeforeEach
    public void setUp() {
        patientDaoImp = Mockito.mock(PatientDaoImp.class);
        patientService = new PatientService(patientDaoImp);
    }

    @Test
    void getByIdTest() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setLastName("Lastname getByIdTest");
        patient.setFirstName("Firstname getByIdTest");
        patient.setPatronymic("Patronymic getByIdTest");
        patient.setJob("Job getByIdTest");

        Mockito.when(patientDaoImp.getById(1L)).thenReturn(patient);

        PatientDTO patientDTO = patientService.getById(1L);

        assertNotNull(patientDTO);
        assertEquals(patient.getId(), patientDTO.getId());
        assertEquals(patient.getLastName(), patientDTO.getLastName());
        assertEquals(patient.getFirstName(), patientDTO.getFirstName());
        assertEquals(patient.getPatronymic(), patientDTO.getPatronymic());
        assertEquals(patient.getJob(), patientDTO.getJob());
    }

    @Test
    void getAllTest() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setLastName("Lastname testGetAllUsers");
        patient.setFirstName("Firstname testGetAllUsers");
        patient.setPatronymic("Patronymic testGetAllUsers");
        patient.setJob("Job testGetAllUsers");

        Mockito.when(patientDaoImp.getAll()).thenReturn(Collections.singletonList(patient));

        List<PatientDTO> patients = patientService.getAll();
        assertEquals(1, patients.size());
        assertEquals("Lastname testGetAllUsers", patients.get(0).getLastName());
    }


    @Test
    void saveTest() throws SQLException {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(1L);
        patientDTO.setLastName("Lastname saveTest");
        patientDTO.setFirstName("Firstname saveTest");
        patientDTO.setPatronymic("Patronymic saveTest");
        patientDTO.setJob("Job saveTest");

        patientService.save(patientDTO);

        Mockito.verify(patientDaoImp, Mockito.times(1)).save(any(Patient.class));
    }

    @Test
    void updateTest() throws SQLException {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(1L);
        patientDTO.setLastName("Lastname saveTest");
        patientDTO.setFirstName("Firstname saveTest");
        patientDTO.setPatronymic("Patronymic saveTest");
        patientDTO.setJob("Job saveTest");

        patientService.update(patientDTO);

        Mockito.verify(patientDaoImp, Mockito.times(1)).update(any(Patient.class));
    }

    @Test
    void deleteByIdTest() {
        patientService.deleteById(1L);

        Mockito.verify(patientDaoImp, Mockito.times(1)).deleteById(1L);
    }
}