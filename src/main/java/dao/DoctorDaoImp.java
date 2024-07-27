package dao;

import model.*;
import model.Doctor;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
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
                        "SELECT lastname, firstname, patronymic, specialization, clinic_id, patients " +
                                "FROM doctor WHERE id = ?");
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    doctor = new Doctor();

                    Array patientsArray = resultSet.getArray("patients");
                    Integer[] patientIds = (Integer[]) patientsArray.getArray();
                    List<Patient> patients = Arrays.stream(patientIds)
                            .map(i -> {
                                Patient patient = new Patient();
                                try {
                                    PreparedStatement statementPatient = connection.prepareStatement(
                                            "SELECT lastname, firstname, patronymic, job " +
                                                    "FROM patient WHERE id = ?");
                                    statementPatient.setLong(1, i);
                                    ResultSet resultSetPatient = statementPatient.executeQuery();
                                    if (resultSetPatient.next()) {
                                        patient.setId(Long.valueOf(i));
                                        patient.setLastName(resultSetPatient.getString("lastname"));
                                        patient.setFirstName(resultSetPatient.getString("firstname"));
                                        patient.setPatronymic(resultSetPatient.getString("patronymic"));
                                        patient.setJob(resultSetPatient.getString("job"));
                                    }
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                return patient;
                            })
                            .toList();

                    Long clinicId = resultSet.getLong("clinic_id");
                    Clinic clinic = new Clinic();
                    try {
                        PreparedStatement statementClinic = connection.prepareStatement("SELECT name_clinic, address, " +
                                "type_clinic FROM clinic WHERE id = ?");
                        statementClinic.setLong(1, clinicId);
                        ResultSet resultSetClinic = statementClinic.executeQuery();
                        if(resultSetClinic.next()) {
                            clinic.setId(clinicId);
                            clinic.setName(resultSetClinic.getString("name_clinic"));
                            clinic.setAddress(resultSetClinic.getString("address"));
                            clinic.setType(resultSetClinic.getString("type_clinic"));
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    doctor.setId(id);
                    doctor.setLastName(resultSet.getString("lastname"));
                    doctor.setFirstName(resultSet.getString("firstname"));
                    doctor.setPatronymic(resultSet.getString("patronymic"));
                    doctor.setSpecialization(resultSet.getString("specialization"));
                    doctor.setPatients(patients);
                    doctor.setClinic(clinic);
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
                    "specialization, clinic_id, patients FROM doctor");
            ResultSet resultSet = statement.executeQuery();
            List<Doctor> doctors = new ArrayList<>();
            while (resultSet.next()) {
                Doctor doctor = new Doctor();

                Array patientsArray = resultSet.getArray("patients");
                Integer[] patientIds = (Integer[]) patientsArray.getArray();
                List<Patient> patients = Arrays.stream(patientIds)
                        .map(i -> {
                            Patient patient = new Patient();
                            try {
                                PreparedStatement statementPatient = connection.prepareStatement(
                                        "SELECT lastname, firstname, patronymic, job " +
                                                "FROM patient WHERE id = ?");
                                statementPatient.setLong(1, i);
                                ResultSet resultSetPatient = statementPatient.executeQuery();
                                if (resultSetPatient.next()) {
                                    patient.setId(Long.valueOf(i));
                                    patient.setLastName(resultSetPatient.getString("lastname"));
                                    patient.setFirstName(resultSetPatient.getString("firstname"));
                                    patient.setPatronymic(resultSetPatient.getString("patronymic"));
                                    patient.setJob(resultSetPatient.getString("job"));
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            return patient;
                        })
                        .toList();

                Long clinicId = resultSet.getLong("clinic_id");
                Clinic clinic = new Clinic();
                try {
                    PreparedStatement statementClinic = connection.prepareStatement(
                            "SELECT name_clinic, address, type_clinic FROM clinic WHERE id = ?");
                    statementClinic.setLong(1, clinicId);
                    ResultSet resultSetClinic = statementClinic.executeQuery();
                    if(resultSetClinic.next()) {
                        clinic.setId(clinicId);
                        clinic.setName(resultSetClinic.getString("name_clinic"));
                        clinic.setAddress(resultSetClinic.getString("address"));
                        clinic.setType(resultSetClinic.getString("type_clinic"));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                doctor.setId(resultSet.getLong("id"));
                doctor.setLastName(resultSet.getString("lastname"));
                doctor.setFirstName(resultSet.getString("firstname"));
                doctor.setPatronymic(resultSet.getString("patronymic"));
                doctor.setSpecialization(resultSet.getString("specialization"));
                doctor.setPatients(patients);
                doctor.setClinic(clinic);

                doctors.add(doctor);
            }
            return doctors;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
                List<Long> idPatients = doctor.getPatients()
                        .stream()
                        .map(People::getId)
                        .toList();
                Array patientIds = connection.createArrayOf("int4", idPatients.toArray());

                PreparedStatement statement = connection.prepareStatement("INSERT INTO doctor (lastname, firstname, " +
                        "patronymic, specialization, clinic_id, patients) " +
                        "VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, doctor.getLastName());
                statement.setString(2, doctor.getFirstName());
                statement.setString(3, doctor.getPatronymic());
                statement.setString(4, doctor.getSpecialization());
                statement.setLong(5, doctor.getClinic().getId());
                statement.setArray(6, patientIds);
                statement.executeUpdate();

                ResultSet resultSet = statement.getGeneratedKeys();
                if(resultSet.next()) {
                    doctor.setId((resultSet.getLong(1)));
                }
            }
        }
    }

    /**
     * The method changes the entity Doctor in the database.
     * @param doctor entity Doctor.
     */
    @Override
    public void update(Doctor doctor) {
        try (Connection connection = source.getConnection()) {
            List<Long> idPatients = doctor.getPatients()
                    .stream()
                    .map(People::getId)
                    .toList();
            Array patientIds = connection.createArrayOf("int4", idPatients.toArray());

            PreparedStatement statement = connection.prepareStatement("UPDATE doctor SET lastname=?, firstname=?, " +
                    "patronymic=?, specialization=?, clinic_id=?, patients=? WHERE id=?");
            statement.setString(1, doctor.getLastName());
            statement.setString(2, doctor.getFirstName());
            statement.setString(3, doctor.getPatronymic());
            statement.setString(4, doctor.getSpecialization());
            statement.setLong(5, doctor.getClinic().getId());
            statement.setArray(6, patientIds);
            statement.setLong(7, doctor.getId());
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
        return true;
    }
}
