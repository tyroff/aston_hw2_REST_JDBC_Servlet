package service;

import dao.DoctorDaoImp;
import dao.PatientDaoImp;
import dto.DoctorDTO;
import dto.PatientDTO;
import model.Doctor;
import model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class DoctorServiceTest {
    private DoctorService doctorService;
    private DoctorDaoImp doctorDaoImp;


    @BeforeEach
    public void setUp() {
        doctorDaoImp = Mockito.mock(DoctorDaoImp.class);
        doctorService = new DoctorService(doctorDaoImp);
    }

    @Test
    void getByIdTest() {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setLastName("Lastname getByIdTest");
        doctor.setFirstName("Firstname getByIdTest");
        doctor.setPatronymic("Patronymic getByIdTest");
        doctor.setSpecialization("Specialization getByIdTest");

        Mockito.when(doctorDaoImp.getById(1L)).thenReturn(doctor);

        DoctorDTO doctorDTO = doctorService.getById(1L);

        assertNotNull(doctorDTO);
        assertEquals(doctor.getId(), doctorDTO.getId());
        assertEquals(doctor.getLastName(), doctorDTO.getLastName());
        assertEquals(doctor.getFirstName(), doctorDTO.getFirstName());
        assertEquals(doctor.getPatronymic(), doctorDTO.getPatronymic());
        assertEquals(doctor.getSpecialization(), doctorDTO.getSpecialization());
    }

    @Test
    void getAllTest() {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setLastName("Lastname getAllTest");
        doctor.setFirstName("Firstname getAllTest");
        doctor.setPatronymic("Patronymic getAllTest");
        doctor.setSpecialization("Specialization getAllTest");

        Mockito.when(doctorDaoImp.getAll()).thenReturn(Collections.singletonList(doctor));

        List<DoctorDTO> doctorDTOS = doctorService.getAll();
        assertEquals(1, doctorDTOS.size());
        assertEquals("Lastname getAllTest", doctorDTOS.get(0).getLastName());
    }

    @Test
    void saveTest() throws SQLException {
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(1L);
        doctorDTO.setLastName("Lastname getAllTest");
        doctorDTO.setFirstName("Firstname getAllTest");
        doctorDTO.setPatronymic("Patronymic getAllTest");
        doctorDTO.setSpecialization("Specialization getAllTest");

        doctorService.save(doctorDTO);

        Mockito.verify(doctorDaoImp, Mockito.times(1)).save(any(Doctor.class));
    }

    @Test
    void updateTest() throws SQLException {
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(1L);
        doctorDTO.setLastName("Lastname getAllTest");
        doctorDTO.setFirstName("Firstname getAllTest");
        doctorDTO.setPatronymic("Patronymic getAllTest");
        doctorDTO.setSpecialization("Specialization getAllTest");

        doctorService.update(doctorDTO);

        Mockito.verify(doctorDaoImp, Mockito.times(1)).update(any(Doctor.class));
    }

    @Test
    void deleteByIdTest() {
        doctorService.deleteById(1L);

        Mockito.verify(doctorDaoImp, Mockito.times(1)).deleteById(1L);
    }

}