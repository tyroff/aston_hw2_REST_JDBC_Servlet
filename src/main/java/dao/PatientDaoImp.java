package dao;

import model.Clinic;
import model.Doctor;
import model.Patient;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDaoImp implements IPatientDao {
    private final DataSource source;

    public PatientDaoImp(DataSource source) {
        this.source = source;
    }

    @Override
    public void save(Patient patient) throws SQLException {
        if (patient != null) {
            try(Connection connection = source.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO patient (lastname, firstname, patronymic, birthday, job, doctors, clinics) " +
                                "VALUES (?,?,?,?,?,?,?)");
                //TODO: add doctors and clinics
                statement.setString(1, patient.getLastName());
                statement.setString(2, patient.getFirstName());
                statement.setString(3, patient.getPatronymic());
                statement.setDate(4, (Date) patient.getBirthday());
                statement.setString(5, patient.getJob());
                statement.setString(6, null);
                statement.setString(7, null);
            }
        }
    }

    @Override
    public Patient getById(Long id) {
        Patient patient = null;
        if (id != null) {
            try(Connection connection = source.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT lastname, firstname, patronymic, birthday, job, doctors, clinics " +
                                "FROM patient WHERE id = ?"
                );
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                patient = createPatient(resultSet);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return patient;
    }

    @Override
    public List<Patient> getAll() {
        try (Connection connection = source.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT lastname, firstname, patronymic, birthday, job, doctors, clinics FROM patient");
            ResultSet resultSet = statement.executeQuery();
            List<Patient> patients = new ArrayList<>();
            while (resultSet.next()) {
                Patient patient = createPatient(resultSet);
                patients.add(patient);
            }
            return patients;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Patient c) {

    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    private Patient createPatient(ResultSet resultSet) throws SQLException {
        Patient patient = new Patient();
        patient.setId(resultSet.getLong("id"));
        patient.setLastName(resultSet.getString("lastname"));
        patient.setFirstName(resultSet.getString("firstname"));
        patient.setPatronymic(resultSet.getString("patronymic"));
        patient.setBirthday(resultSet.getDate("birthday"));
        patient.setJob(resultSet.getString("job"));
        //TODO: add doctors and clinics
        patient.setDoctors(null);
        patient.setClinics(null);
        return patient;
    }
}
