package dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import model.Clinic;
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

class ClinicDaoImpTest {
    public static ClinicDaoImp clinicDaoImp;
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
        clinicDaoImp = new ClinicDaoImp(source);
    }

    @Test
    void getByIdTest() throws SQLException {
        Clinic clinic = new Clinic();
        clinic.setName("setName");
        clinic.setAddress("setAddress");
        clinic.setType("setType");

        clinicDaoImp.save(clinic);

        Clinic extractClinic = clinicDaoImp.getById((clinic.getId()));
        assertNotNull(extractClinic);
        assertEquals(clinic, extractClinic);
    }

    @Test
    void getAllTest() throws SQLException {
        List<Clinic> patients = new ArrayList<>();
        Clinic clinic1 = new Clinic();
        clinic1.setName("setName1");
        clinic1.setAddress("setAddress1");
        clinic1.setType("setType1");
        clinicDaoImp.save(clinic1);
        patients.add(clinic1);

        Clinic clinic2 = new Clinic();
        clinic2.setName("setName2");
        clinic2.setAddress("setAddress2");
        clinic2.setType("setType2");
        clinicDaoImp.save(clinic2);
        patients.add(clinic2);

        List<Clinic> extractpatients = clinicDaoImp.getAll();

        assertNotNull(patients);
        assertNotNull(extractpatients);
    }

    @Test
    void saveTest() throws SQLException {
        Clinic clinic = new Clinic();
        clinic.setName("setName saveTest");
        clinic.setAddress("setAddress saveTest");
        clinic.setType("setType saveTest");
        clinicDaoImp.save(clinic);

        Clinic extractClinic = clinicDaoImp.getById((clinic.getId()));
        assertNotNull(extractClinic);
        assertEquals(extractClinic.getName(), "setName saveTest");
        assertEquals(extractClinic.getAddress(), "setAddress saveTest");
        assertEquals(extractClinic.getType(), "setType saveTest");
    }

    @Test
    void updateTest() throws SQLException {
        Clinic clinic = new Clinic();
        clinic.setName("setName updateTest");
        clinic.setAddress("setAddress updateTest");
        clinic.setType("setType updateTest");
        clinicDaoImp.save(clinic);

        Clinic extractClinic = clinicDaoImp.getById((clinic.getId()));
        Long extractClinicId = extractClinic.getId();
        extractClinic.setId(extractClinicId);
        extractClinic.setName("New setName updateTest");
        extractClinic.setAddress("New setAddress updateTest");
        extractClinic.setType("New setType updateTest");

        clinicDaoImp.update(extractClinic);

        assertEquals(extractClinic, clinicDaoImp.getById(extractClinicId));
    }

    @Test
    void deleteByIdTest() throws SQLException {
        Clinic clinic = new Clinic();
        clinic.setName("setName deleteByIdTest");
        clinic.setAddress("setAddress deleteByIdTest");
        clinic.setType("setType deleteByIdTest");
        clinicDaoImp.save(clinic);

        Clinic extractClinic = clinicDaoImp.getById((clinic.getId()));
        Long extractClinicId = extractClinic.getId();

        clinicDaoImp.deleteById(extractClinicId);

        assertNull(clinicDaoImp.getById(extractClinicId));
    }
}