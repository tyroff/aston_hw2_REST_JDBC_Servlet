package dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import model.Doctor;
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

class DoctorDaoImpTest {
    public static DoctorDaoImp doctorDaoImp;
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
        doctorDaoImp = new DoctorDaoImp(source);
    }

    @Test
    void getByIdTest() throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setLastName("Lastname getByIdTest");
        doctor.setFirstName("Firstname getByIdTest");
        doctor.setPatronymic("Patronymic getByIdTest");
        doctor.setSpecialization("Specialization getByIdTest");
        doctorDaoImp.save(doctor);

        Doctor extractDoctor = doctorDaoImp.getById((doctor.getId()));
        assertNotNull(extractDoctor);
        assertEquals(doctor, extractDoctor);
    }

    @Test
    void getAllTest() throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        Doctor doctor1 = new Doctor();
        doctor1.setLastName("Lastname1");
        doctor1.setFirstName("Firstname1");
        doctor1.setPatronymic("Patronymic1");
        doctor1.setSpecialization("Specialization1");
        doctorDaoImp.save(doctor1);

        Doctor doctor2 = new Doctor();
        doctor2.setLastName("Lastname2");
        doctor2.setFirstName("Firstname2");
        doctor2.setPatronymic("Patronymic2");
        doctor2.setSpecialization("Specialization2");
        doctorDaoImp.save(doctor2);

        doctors.add(doctorDaoImp.getById(doctor1.getId()));
        doctors.add(doctorDaoImp.getById(doctor2.getId()));

        assertNotNull(doctors);
    }

    @Test
    void saveTest() throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setLastName("Lastname saveTest");
        doctor.setFirstName("Firstname saveTest");
        doctor.setPatronymic("Patronymic saveTest");
        doctor.setSpecialization("specialization saveTest");
        doctorDaoImp.save(doctor);

        Doctor extractDoctor = doctorDaoImp.getById((doctor.getId()));
        assertNotNull(extractDoctor);
        assertEquals(extractDoctor.getLastName(), "Lastname saveTest");
        assertEquals(extractDoctor.getFirstName(), "Firstname saveTest");
        assertEquals(extractDoctor.getPatronymic(), "Patronymic saveTest");
        assertEquals(extractDoctor.getSpecialization(), "specialization saveTest");
    }

    @Test
    void updateTest() throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setLastName("Lastname updateTest");
        doctor.setFirstName("Firstname updateTest");
        doctor.setPatronymic("Patronymic updateTest");
        doctor.setSpecialization("Specialization updateTest");
        doctorDaoImp.save(doctor);

        Doctor extractDoctor = doctorDaoImp.getById((doctor.getId()));
        Long extractDoctorId = extractDoctor.getId();
        extractDoctor.setLastName("New Lastname updateTest");
        extractDoctor.setFirstName("New Firstname updateTest");
        extractDoctor.setPatronymic("New Patronymic updateTest");
        extractDoctor.setSpecialization("New Specialization updateTest");

        doctorDaoImp.update(extractDoctor);

        assertEquals(extractDoctor, doctorDaoImp.getById(extractDoctorId));
    }

    @Test
    void deleteByIdTest() throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setLastName("Lastname deleteByIdTest");
        doctor.setFirstName("Firstname deleteByIdTest");
        doctor.setPatronymic("Patronymic deleteByIdTest");
        doctor.setSpecialization("Specialization deleteByIdTest");
        doctorDaoImp.save(doctor);

        Doctor extractDoctor = doctorDaoImp.getById((doctor.getId()));
        Long extractDoctorId = extractDoctor.getId();

        doctorDaoImp.deleteById(extractDoctorId);

        assertNull(doctorDaoImp.getById(extractDoctorId));
    }
}