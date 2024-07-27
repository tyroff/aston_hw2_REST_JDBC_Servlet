package dao;

import model.Clinic;
import model.Doctor;
import model.Patient;
import model.People;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClinicDaoImp implements IClinicDao {
    private final DataSource source;

    /**
     * Constructor with the entity parameter DataSource.
     *
     * @param source entity parameter DataSource.
     */

    public ClinicDaoImp(DataSource source) {
        this.source = source;
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
                        "SELECT name_clinic, address, type_clinic, doctors, patients FROM clinic WHERE id = ?"
                );
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    clinic = new Clinic();

                    Array doctorsArray = resultSet.getArray("doctors");
                    Integer[] doctorIds = (Integer[]) doctorsArray.getArray();
                    List<Doctor> doctors = Arrays.stream(doctorIds)
                            .map(i -> {
                                Doctor doctor = new Doctor();
                                try {
                                    PreparedStatement statementDoctor = connection.prepareStatement(
                                            "SELECT lastname, firstname, patronymic, specialization " +
                                                    "FROM doctor WHERE id = ?"

                                    );
                                    statementDoctor.setLong(1, i);
                                    ResultSet resultSetDoctor = statementDoctor.executeQuery();
                                    if (resultSetDoctor.next()) {
                                        doctor.setId(Long.valueOf(i));
                                        doctor.setLastName(resultSetDoctor.getString("lastname"));
                                        doctor.setFirstName(resultSetDoctor.getString("firstname"));
                                        doctor.setPatronymic(resultSetDoctor.getString("patronymic"));
                                        doctor.setSpecialization(resultSetDoctor.getString("specialization"));
                                    }
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                return doctor;
                            })
                            .toList();

                    Array patientsArray = resultSet.getArray("patients");
                    Integer[] patientIds = (Integer[]) patientsArray.getArray();
                    List<Patient> patients = Arrays.stream(patientIds)
                            .map(i -> {
                                Patient patient = new Patient();
                                try {
                                    PreparedStatement statementPatient = connection.prepareStatement(
                                            "SELECT lastname, firstname, patronymic, job " +
                                                    "FROM patient WHERE id = ?"

                                    );
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

                    clinic.setId(id);
                    clinic.setName(resultSet.getString("name_clinic"));
                    clinic.setAddress(resultSet.getString("address"));
                    clinic.setType(resultSet.getString("type_clinic"));
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
            PreparedStatement statement = connection.prepareStatement("SELECT id, name_clinic, address, type_clinic, doctors, patients FROM clinic");
            ResultSet resultSet = statement.executeQuery();
            List<Clinic> clinics = new ArrayList<>();
            while (resultSet.next()) {
                Clinic clinic = new Clinic();

                Array doctorsArray = resultSet.getArray("doctors");
                Integer[] doctorIds = (Integer[]) doctorsArray.getArray();
                List<Doctor> doctors = Arrays.stream(doctorIds)
                        .map(i -> {
                            Doctor doctor = new Doctor();
                            try {
                                PreparedStatement statementDoctor = connection.prepareStatement(
                                        "SELECT lastname, firstname, patronymic, specialization " +
                                                "FROM doctor WHERE id = ?"

                                );
                                statementDoctor.setLong(1, i);
                                ResultSet resultSetDoctor = statementDoctor.executeQuery();
                                if (resultSetDoctor.next()) {
                                    doctor.setId(Long.valueOf(i));
                                    doctor.setLastName(resultSetDoctor.getString("lastname"));
                                    doctor.setFirstName(resultSetDoctor.getString("firstname"));
                                    doctor.setPatronymic(resultSetDoctor.getString("patronymic"));
                                    doctor.setSpecialization(resultSetDoctor.getString("specialization"));
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            return doctor;
                        })
                        .toList();

                Array patientsArray = resultSet.getArray("patients");
                Integer[] patientIds = (Integer[]) patientsArray.getArray();
                List<Patient> patients = Arrays.stream(patientIds)
                        .map(i -> {
                            Patient patient = new Patient();
                            try {
                                PreparedStatement statementPatient = connection.prepareStatement(
                                        "SELECT lastname, firstname, patronymic, job " +
                                                "FROM patient WHERE id = ?"

                                );
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

                clinic.setId(resultSet.getLong("id"));
                clinic.setName(resultSet.getString("name_clinic"));
                clinic.setAddress(resultSet.getString("address"));
                clinic.setType(resultSet.getString("type_clinic"));
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
                Array doctorIds = connection.createArrayOf("int4", idDoctors.toArray());

                List<Long> idPatients = clinic.getPatients()
                        .stream()
                        .map(People::getId)
                        .toList();
                Array patientIds = connection.createArrayOf("int4", idPatients.toArray());

                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO clinic (name_clinic, address, type_clinic, doctors,  patients) " +
                                "VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

                statement.setString(1, clinic.getName());
                statement.setString(2, clinic.getAddress());
                statement.setString(3, clinic.getType());
                statement.setArray(4, doctorIds);
                statement.setArray(5, patientIds);
                statement.executeUpdate();

                ResultSet resultSet = statement.getGeneratedKeys();

                if(resultSet.next()) {
                    clinic.setId((resultSet.getLong(1)));
                }
            }
        }
    }

    /**
     * The method changes the entity Clinic in the database.
     * @param clinic entity Clinic.
     */
    @Override
    public void update(Clinic clinic) {
        try (Connection connection = source.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE clinic SET name_clinic = ?, address = ?, " +
                    "type_clinic = ?, doctors = ?, patients = ? WHERE id = ?");

            List<Long> idDoctors = clinic.getDoctors()
                    .stream()
                    .map(People::getId)
                    .toList();
            Array doctorIds = connection.createArrayOf("int4", idDoctors.toArray());

            List<Long> idPatients = clinic.getPatients()
                    .stream()
                    .map(People::getId)
                    .toList();
            Array patientIds = connection.createArrayOf("int4", idPatients.toArray());

            statement.setString(1, clinic.getName());
            statement.setString(2, clinic.getAddress());
            statement.setString(3, clinic.getType());
            statement.setArray(4, doctorIds);
            statement.setArray(5, patientIds);
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
        return true;
    }
}
