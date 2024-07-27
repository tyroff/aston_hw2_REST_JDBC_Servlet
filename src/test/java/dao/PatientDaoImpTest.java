package dao;

import model.Patient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.DataPropertiesUtil;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatientDaoImpTest {
    static PatientDaoImp patientDaoImp;
    static DataSource source;

    @BeforeAll
    public static void setUp() {
        source = DataPropertiesUtil.getDataSource;
        patientDaoImp = new PatientDaoImp(source);
    }

    @Test
    void saveTest() throws SQLException {
        Patient patient = new Patient();
        patient.setLastName("Lastname");
        patient.setFirstName("Firstname");
        patient.setPatronymic("Patronymic");
        patient.setJob("Job");
        patientDaoImp.save(patient);

        Patient extractPatient = patientDaoImp.getById((patient.getId()));
        assertNotNull(extractPatient);
        assertEquals(extractPatient.getLastName(), "Lastname");
        assertEquals(extractPatient.getFirstName(), "Firstname");
        assertEquals(extractPatient.getPatronymic(), "Patronymic");
        assertEquals(extractPatient.getJob(), "Job");
    }

    @Test
    void getByIdTest() throws SQLException {
        Patient patient = new Patient();
        patient.setLastName("Lastname");
        patient.setFirstName("Firstname");
        patient.setPatronymic("Patronymic");
        patient.setJob("Job");
        patientDaoImp.save(patient);

        Patient extractPatient = patientDaoImp.getById((patient.getId()));
        assertNotNull(extractPatient);
        assertEquals(patient, extractPatient);
    }

    @Test
    void getAllTest() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        Patient patient1 = new Patient();
        patient1.setLastName("Lastname1");
        patient1.setFirstName("Firstname1");
        patient1.setPatronymic("Patronymic1");
        patient1.setJob("Job1");
        patientDaoImp.save(patient1);

        Patient patient2 = new Patient();
        patient2.setLastName("Lastname2");
        patient2.setFirstName("Firstname2");
        patient2.setPatronymic("Patronymic2");
        patient2.setJob("Job2");
        patientDaoImp.save(patient2);

        patients = patientDaoImp.getAll();
        assertNotNull(patients);
    }

    @Test
    void updateTest() throws SQLException {
        Patient patient = new Patient();
        patient.setLastName("Lastname");
        patient.setFirstName("Firstname");
        patient.setPatronymic("Patronymic");
        patient.setJob("Job");
        patientDaoImp.save(patient);

        Patient extractPatient = patientDaoImp.getById((patient.getId()));
        Long extractPatientId = extractPatient.getId();
        extractPatient.setLastName("NewLastname");
        extractPatient.setFirstName("NewFirstname");
        extractPatient.setPatronymic("NewPatronymic");
        extractPatient.setJob("NewJob");

        patientDaoImp.update(extractPatient);

        assertEquals(extractPatient, patientDaoImp.getById(extractPatientId));
    }

    @Test
    void deleteByIdTest() throws SQLException {
        Patient patient = new Patient();
        patient.setLastName("Lastname");
        patient.setFirstName("Firstname");
        patient.setPatronymic("Patronymic");
        patient.setJob("Job");
        patientDaoImp.save(patient);

        Patient extractPatient = patientDaoImp.getById((patient.getId()));
        Long extractPatientId = extractPatient.getId();

        patientDaoImp.deleteById(extractPatientId);

        assertNull(patientDaoImp.getById(extractPatientId));
    }
}