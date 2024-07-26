package dao;

import model.Clinic;
import model.Doctor;
import model.Patient;
import model.People;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClinicDaoImp implements IClinicDao {
    private final DataSource source;
    private final DoctorDaoImp doctorDaoImp;
    private final PatientDaoImp patientDaoImp;

    /**
     * Constructor with the entity parameter DataSource.
     *
     * @param source entity parameter DataSource.
     * @param doctorDaoImp entity DoctorDaoImp.
     * @param patientDaoImp entity PatientDaoImp.
     */
    public ClinicDaoImp(DataSource source, DoctorDaoImp doctorDaoImp, PatientDaoImp patientDaoImp) {
        this.source = source;
        this.doctorDaoImp = doctorDaoImp;
        this.patientDaoImp = patientDaoImp;
    }

    /**
     * The saves in the database the entity Clinic.
     * @param clinic the entity Clinic.
     * @throws SQLException
     */
    @Override
    public void save(Clinic clinic) throws SQLException {
        if (clinic != null) {
            try(Connection connection = source.getConnection()) {
                List<Long> idDoctors = clinic.getDoctors()
                        .stream()
                        .map(People::getId)
                        .toList();

                List<Long> idPatients = clinic.getPatients()
                        .stream()
                        .map(People::getId)
                        .toList();

                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO clinic (name, address, type, doctors,  patients) " +
                        "VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

                statement.setString(1, clinic.getName());
                statement.setString(2, clinic.getAddress());
                statement.setString(3, clinic.getType());
                statement.setArray(4, (Array) idDoctors);
                statement.setArray(5, (Array) idPatients);
                statement.executeUpdate();

                ResultSet resultSet = statement.getGeneratedKeys();

                if(resultSet.next()) {
                    clinic.setId((resultSet.getLong(1)));
                }
            }
        }
    }

    /**
     * Returns one entity Clinic by id otherwise null.
     * @param id id entity Clinic.
     * @return entity Clinic.
     */
    @Override
    public Clinic getById(Long id) {
        Clinic clinic = null;
        if (id != null) {
            try(Connection connection = source.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT name, address, type, doctors, patients FROM clinic WHERE id = ?"
                );
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    clinic = new Clinic();

                    List<Long> idDoctors = (List<Long>) resultSet.getArray("doctors");
                    List<Doctor> doctors = idDoctors.stream().map(doctorDaoImp::getById).toList();

                    List<Long> idPatients = (List<Long>) resultSet.getArray("patients");
                    List<Patient> patients = idPatients.stream().map(patientDaoImp::getById).toList();

                    clinic.setId(id);
                    clinic.setName(resultSet.getString("name"));
                    clinic.setAddress(resultSet.getString("address"));
                    clinic.setType(resultSet.getString("type"));
                    clinic.setDoctors(doctors);
                    clinic.setPatients(patients);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return clinic;
    }

    /**
     * Returns a list of all entity Clinic otherwise null.
     * @return List<Clinic>
     */
    @Override
    public List<Clinic> getAll() {
        try (Connection connection = source.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT id, name, address, type, doctors, patients FROM clinic");
            ResultSet resultSet = statement.executeQuery();
            List<Clinic> clinics = new ArrayList<>();
            while (resultSet.next()) {
                Clinic clinic = new Clinic();

                List<Long> idDoctors = (List<Long>) resultSet.getArray("doctors");
                List<Doctor> doctors = idDoctors.stream().map(doctorDaoImp::getById).toList();

                List<Long> idPatients = (List<Long>) resultSet.getArray("patients");
                List<Patient> patients = idPatients.stream().map(patientDaoImp::getById).toList();

                clinic.setId(resultSet.getLong("id"));
                clinic.setName(resultSet.getString("name"));
                clinic.setAddress(resultSet.getString("address"));
                clinic.setType(resultSet.getString("type"));
                clinic.setDoctors(doctors);
                clinic.setPatients(patients);

                clinics.add(clinic);
            }
            return clinics;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The method changes the entity Clinic in the database.
     * @param clinic entity Clinic.
     */
    @Override
    public void update(Clinic clinic) {
        try (Connection connection = source.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE clinic SET name = ?, address = ?, " +
                    "type = ?, doctors = ?, patients = ? WHERE id = ?");

            List<Long> idDoctors = clinic.getDoctors()
                    .stream()
                    .map(People::getId)
                    .toList();

            List<Long> idPatients = clinic.getPatients()
                    .stream()
                    .map(People::getId)
                    .toList();

            statement.setString(1, clinic.getName());
            statement.setString(2, clinic.getAddress());
            statement.setString(3, clinic.getType());
            statement.setString(4, String.valueOf(idDoctors));
            statement.setString(5, String.valueOf(idPatients));
            statement.setLong(6, clinic.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The method removes the id Clinic entity in the database.
     * @param id id entity Clinic.
     * @return  true if the id Clinic entity was deleted from the database otherwise false.
     */
    @Override
    public boolean deleteById(Long id) {
        try (Connection connection = source.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM clinic WHERE id = ?");
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
