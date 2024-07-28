package dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import model.Patient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static util.DataPropertiesUtil.loadProperties;

class PatientDaoImpTest {
    public static PatientDaoImp patientDaoImp;
    public static DataSource source;
    private static final HikariConfig config = new HikariConfig();

    @BeforeAll
    public static void setUp() {
        try {
            Properties properties = loadProperties();
            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.username"));
            config.setPassword(properties.getProperty("db.password"));
            config.setDriverClassName(properties.getProperty("db.driver-class-name"));
            source = new HikariDataSource(config);
        } catch (IOException e) {
            throw new RuntimeException("DBPropertiesUtil class initialization failed!", e);
        }
        patientDaoImp = new PatientDaoImp(source);
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
    void saveTest() throws SQLException {
        Patient patient = new Patient();
        patient.setLastName("Lastname saveTest");
        patient.setFirstName("Firstname saveTest");
        patient.setPatronymic("Patronymic saveTest");
        patient.setJob("Job saveTest");
        patientDaoImp.save(patient);

        Patient extractPatient = patientDaoImp.getById((patient.getId()));
        assertNotNull(extractPatient);
        assertEquals(extractPatient.getLastName(), "Lastname saveTest");
        assertEquals(extractPatient.getFirstName(), "Firstname saveTest");
        assertEquals(extractPatient.getPatronymic(), "Patronymic saveTest");
        assertEquals(extractPatient.getJob(), "Job saveTest");
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
        patient.setLastName("Lastname deleteByIdTest");
        patient.setFirstName("Firstname deleteByIdTest");
        patient.setPatronymic("Patronymic deleteByIdTest");
        patient.setJob("Job deleteByIdTest");
        patientDaoImp.save(patient);

        Patient extractPatient = patientDaoImp.getById((patient.getId()));
        Long extractPatientId = extractPatient.getId();

        patientDaoImp.deleteById(extractPatientId);

        assertNull(patientDaoImp.getById(extractPatientId));
    }
}