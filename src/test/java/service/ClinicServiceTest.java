package service;

import dao.ClinicDaoImp;
import dao.DoctorDaoImp;
import dto.ClinicDTO;
import dto.DoctorDTO;
import model.Clinic;
import model.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class ClinicServiceTest {
    private ClinicService clinicService;
    private ClinicDaoImp clinicDaoImp;


    @BeforeEach
    public void setUp() {
        clinicDaoImp = Mockito.mock(ClinicDaoImp.class);
        clinicService = new ClinicService(clinicDaoImp);
    }

    @Test
    void getByIdTest() throws SQLException {
        Clinic clinic = new Clinic();
        clinic.setName("setName getByIdTest");
        clinic.setAddress("setAddress getByIdTest");
        clinic.setType("setType getByIdTest");
        clinicDaoImp.save(clinic);

        Mockito.when(clinicDaoImp.getById(1L)).thenReturn(clinic);

        ClinicDTO clinicDTO = clinicService.getById(1L);

        assertNotNull(clinicDTO);
        assertEquals(clinic.getId(), clinicDTO.getId());
        assertEquals(clinic.getName(), clinicDTO.getName());
        assertEquals(clinic.getAddress(), clinicDTO.getAddress());
        assertEquals(clinic.getType(), clinicDTO.getType());
    }

    @Test
    void getAllTest() {
        Clinic clinic = new Clinic();
        clinic.setId(1L);
        clinic.setName("setName getByIdTest");
        clinic.setAddress("setAddress getByIdTest");
        clinic.setType("setType getByIdTest");

        Mockito.when(clinicDaoImp.getAll()).thenReturn(Collections.singletonList(clinic));

        List<ClinicDTO> clinicDTOS = clinicService.getAll();
        assertEquals(1, clinicDTOS.size());
        assertEquals(clinic.getName(), clinicDTOS.get(0).getName());
    }

    @Test
    void saveTest() throws SQLException {
        ClinicDTO clinicDTO = new ClinicDTO();
        clinicDTO.setId(1L);
        clinicDTO.setName("setName saveTest");
        clinicDTO.setAddress("setAddress saveTest");
        clinicDTO.setType("setType saveTest");

        clinicService.save(clinicDTO);

        Mockito.verify(clinicDaoImp, Mockito.times(1)).save(any(Clinic.class));
    }

    @Test
    void updateTest() throws SQLException {
        ClinicDTO clinicDTO = new ClinicDTO();
        clinicDTO.setId(1L);
        clinicDTO.setName("setName updateTest");
        clinicDTO.setAddress("setAddress updateTest");
        clinicDTO.setType("setType updateTest");

        clinicService.update(clinicDTO);

        Mockito.verify(clinicDaoImp, Mockito.times(1)).update(any(Clinic.class));
    }

    @Test
    void deleteByIdTest() {
        clinicService.deleteById(1L);

        Mockito.verify(clinicDaoImp, Mockito.times(1)).deleteById(1L);
    }
}