package dao;

import model.Doctor;
import model.Doctor;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDaoImp implements IDoctorDao {
    private final DataSource source;

    /**
     * Constructor with the entity parameter DataSource.
     * @param source entity parameter DataSource.
     */
    public DoctorDaoImp(DataSource source) {
        this.source = source;
    }

    /**
     * The saves in the database the entity Doctor.
     * @param doctor the entity Doctor.
     * @throws SQLException
     */
    @Override
    public void save(Doctor doctor) throws SQLException {
        if (doctor != null) {
            try(Connection connection = source.getConnection()) {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO doctor (lastname, firstname, " +
                        "patronymic, specialization) " +
                        "VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                //TODO: add patients and clinic
                statement.setString(1, doctor.getLastName());
                statement.setString(2, doctor.getFirstName());
                statement.setString(3, doctor.getPatronymic());
                statement.setString(4, doctor.getSpecialization());
                statement.executeUpdate();

                ResultSet resultSet = statement.getGeneratedKeys();
                if(resultSet.next()) {
                    doctor.setId((resultSet.getLong(1)));
                }
            }
        }
    }

    /**
     * Returns one entity Doctor by id otherwise null.
     * @param id id entity Doctor.
     * @return entity Doctor.
     */
    @Override
    public Doctor getById(Long id) {
        Doctor doctor = null;
        if (id != null) {
            try(Connection connection = source.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT lastname, firstname, patronymic, specialization" +
                                "FROM doctor WHERE id = ?"
                );
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    doctor = new Doctor();
                    doctor.setId(id);
                    doctor.setLastName(resultSet.getString("lastname"));
                    doctor.setFirstName(resultSet.getString("firstname"));
                    doctor.setPatronymic(resultSet.getString("patronymic"));
                    doctor.setSpecialization(resultSet.getString("specialization"));
                    //TODO: add patients and clinic
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return doctor;
    }

    /**
     * Returns a list of all entity Doctor otherwise null.
     * @return List<Doctor>
     */
    @Override
    public List<Doctor> getAll() {
        try (Connection connection = source.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT id, lastname, firstname, patronymic, " +
                    "specialization FROM doctor");
            ResultSet resultSet = statement.executeQuery();
            List<Doctor> doctors = new ArrayList<>();
            while (resultSet.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(resultSet.getLong("id"));
                doctor.setLastName(resultSet.getString("lastname"));
                doctor.setFirstName(resultSet.getString("firstname"));
                doctor.setPatronymic(resultSet.getString("patronymic"));
                doctor.setSpecialization(resultSet.getString("specialization"));
                //TODO: add doctors and clinics
                doctors.add(doctor);
            }
            return doctors;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The method changes the entity Doctor in the database.
     * @param doctor entity Doctor.
     */
    @Override
    public void update(Doctor doctor) {
        try (Connection connection = source.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE doctor SET lastname = ?, firstname = ?, " +
                    "patronymic = ?, specialization = ? WHERE id = ?");
            statement.setString(1, doctor.getLastName());
            statement.setString(2, doctor.getFirstName());
            statement.setString(3, doctor.getPatronymic());
            statement.setString(4, doctor.getSpecialization());
            //TODO: add patients and clinic
            statement.setLong(5, doctor.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The method removes the id Doctor entity in the database.
     * @param id id entity Doctor.
     * @return  true if the id Doctor entity was deleted from the database otherwise false.
     */
    @Override
    public boolean deleteById(Long id) {
        try (Connection connection = source.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM doctor WHERE id = ?");
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
