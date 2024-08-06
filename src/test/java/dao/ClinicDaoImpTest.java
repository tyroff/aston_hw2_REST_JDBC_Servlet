package dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import model.Clinic;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class ClinicDaoImpTest {
    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("bdTest")
            .withUsername("userTest")
            .withPassword("passwordTest");

    private static ClinicDaoImp clinicDaoImp;
    private static DataSource source;

    @BeforeAll
    public static void setUp() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgresContainer.getJdbcUrl());
        config.setUsername(postgresContainer.getUsername());
        config.setPassword(postgresContainer.getPassword());
        config.setDriverClassName("org.postgresql.Driver");

        source = new HikariDataSource(config);
        clinicDaoImp = new ClinicDaoImp(source);

        try (var connection = source.getConnection(); var statement = connection.createStatement()) {
            statement.execute(
                    """
                    create table if not exists clinic(
                        id int4 generated by default as identity primary key,
                        name_clinic varchar(200) not null,
                        address varchar(200) not null,
                        type_clinic varchar(100) not null,
                        doctors int4[],
                        patients int4[])
                    """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    public static void start() {
        postgresContainer.start();
    }

    @AfterAll
    public static void stop() {
        postgresContainer.stop();
    }

    @BeforeEach
    public void cleanData() {
        try (var connection = source.getConnection(); var statement = connection.createStatement()) {
            statement.execute("delete from clinic");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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