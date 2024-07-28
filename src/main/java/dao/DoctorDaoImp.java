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
                    List<Patient> patients = getAllPatientsByDoctorId(id);
                    Clinic clinic = getClinicById(resultSet.getLong("clinic_id"));

                    doctor = new Doctor();
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
                Array patientIds = null;
                if(doctor.getPatients() != null) {
                    List<Long> idPatients = doctor.getPatients()
                            .stream()
                            .map(People::getId)
                            .toList();
                    patientIds = connection.createArrayOf("int4", idPatients.toArray());
                }

                PreparedStatement statement = connection.prepareStatement("INSERT INTO doctor (lastname, firstname, " +
                        "patronymic, specialization, clinic_id, patients) " +
                        "VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, doctor.getLastName());
                statement.setString(2, doctor.getFirstName());
                statement.setString(3, doctor.getPatronymic());
                statement.setString(4, doctor.getSpecialization());
                if (doctor.getClinic() == null) {
                    statement.setNull(5, Types.INTEGER);
                } else {
                    statement.setLong(5, doctor.getClinic().getId());
                }
                if(patientIds == null) {
                    statement.setNull(6, Types.ARRAY);
                } else {
                    statement.setArray(6, patientIds);
                }
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
            Array patientIds = null;
            if(doctor.getPatients() != null) {
                List<Long> idPatients = doctor.getPatients()
                        .stream()
                        .map(People::getId)
                        .toList();
                patientIds = connection.createArrayOf("int4", idPatients.toArray());
            }

            PreparedStatement statement = connection.prepareStatement("UPDATE doctor SET lastname=?, firstname=?, " +
                    "patronymic=?, specialization=?, clinic_id=?, patients=? WHERE id=?");
            statement.setString(1, doctor.getLastName());
            statement.setString(2, doctor.getFirstName());
            statement.setString(3, doctor.getPatronymic());
            statement.setString(4, doctor.getSpecialization());
            if(doctor.getClinic() == null) {
                statement.setNull(5, Types.INTEGER);
            } else {
                statement.setLong(5, doctor.getClinic().getId());
            }
            if(patientIds == null) {
                statement.setNull(6, Types.ARRAY);
            } else {
                statement.setArray(6, patientIds);
            }
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

    public Clinic getClinicById(Long id) throws SQLException {
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

    public List<Patient> getAllPatientsByDoctorId(Long id) throws SQLException {
        List<Patient> patients = new ArrayList<>();
        try (Connection connection = source.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT patients FROM doctor WHERE id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Array patientsArray = resultSet.getArray("patients");
                if(patientsArray != null) {
                    Integer[] patientIds = (Integer[]) patientsArray.getArray();
                    patients = Arrays.stream(patientIds)
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
                }
            }
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
