package dao;

import model.Patient;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientDaoImp implements IPatientDao {
    private final DataSource source;

    /**
     * Constructor with the entity parameter DataSource.
     * @param source entity parameter DataSource.
     */
    public PatientDaoImp(DataSource source) {
        this.source = source;
    }

    /**
     * The saves in the database the entity Patient.
     * @param patient the entity Patient.
     * @throws SQLException
     */
    @Override
    public void save(Patient patient) throws SQLException {
        if (patient != null) {
            try(Connection connection = source.getConnection()) {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO patient (lastname, firstname, " +
                        "patronymic, job) " +
                                "VALUES (?,?,?,?)");
                //TODO: add doctors and clinics
                statement.setString(1, patient.getLastName());
                statement.setString(2, patient.getFirstName());
                statement.setString(3, patient.getPatronymic());
                statement.setString(4, patient.getJob());
                statement.executeUpdate();
            }
        }
    }

    /**
     * Returns one entity Patient by id otherwise null.
     * @param id id entity Patient.
     * @return entity Patient.
     */
    @Override
    public Patient getById(Long id) {
        Patient patient = null;
        if (id != null) {
            try(Connection connection = source.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT lastname, firstname, patronymic, job " +
                                "FROM patient WHERE id = ?"
                );
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    patient = new Patient();
                    patient.setId(id);
                    patient.setLastName(resultSet.getString("lastname"));
                    patient.setFirstName(resultSet.getString("firstname"));
                    patient.setPatronymic(resultSet.getString("patronymic"));
                    patient.setJob(resultSet.getString("job"));
                    //TODO: add doctors and clinics
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return patient;
    }

    /**
     * Returns a list of all entity Patient otherwise null.
     * @return List<Patient>
     */
    @Override
    public List<Patient> getAll() {
        try (Connection connection = source.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT id, lastname, firstname, patronymic, " +
                    "job FROM patient");
            ResultSet resultSet = statement.executeQuery();
            List<Patient> patients = new ArrayList<>();
            while (resultSet.next()) {
                Patient patient = new Patient();
                patient.setId(resultSet.getLong("id"));
                patient.setLastName(resultSet.getString("lastname"));
                patient.setFirstName(resultSet.getString("firstname"));
                patient.setPatronymic(resultSet.getString("patronymic"));
                patient.setJob(resultSet.getString("job"));
                //TODO: add doctors and clinics
                patients.add(patient);
            }
            return patients;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The method changes the entity Patient in the database.
     * @param patient entity Patient.
     */
    @Override
    public void update(Patient patient) {
        try (Connection connection = source.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE patient SET lastname = ?, firstname = ?, " +
                    "patronymic = ?, job = ? WHERE id = ?");
            statement.setString(1, patient.getLastName());
            statement.setString(2, patient.getFirstName());
            statement.setString(3, patient.getPatronymic());
            statement.setString(4, patient.getJob());
            //TODO: add doctors and clinicsstn
            statement.setLong(5, patient.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The method removes the id Patient entity in the database.
     * @param id id entity Patient.
     * @return  true if the id Patient entity was deleted from the database otherwise false.
     */
    @Override
    public boolean deleteById(Long id) {
        try (Connection connection = source.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM patient WHERE id = ?");
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
