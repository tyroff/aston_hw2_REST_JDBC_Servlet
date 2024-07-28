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

public class PatientDaoImp implements IPatientDao {
    private final DataSource source;

    /**
     * Constructor with the entity parameter DataSource.
     *
     * @param source       entity parameter DataSource.
     */
    public PatientDaoImp(DataSource source) {
        this.source = source;
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
                        "SELECT lastname, firstname, patronymic, job, doctors, clinics " +
                                "FROM patient WHERE id = ?");
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    patient = new Patient();

                    List<Doctor> doctors = null;
                    Array doctorsArray = resultSet.getArray("doctors");
                    if(doctorsArray != null) {
                        Integer[] doctorIds = (Integer[]) doctorsArray.getArray();
                        doctors = Arrays.stream(doctorIds)
                                .map(i -> {
                                    Doctor doctor = new Doctor();
                                    try {
                                        PreparedStatement statementDoctor = connection.prepareStatement(
                                                "SELECT lastname, firstname, patronymic, specialization " +
                                                        "FROM doctor WHERE id = ?");
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
                    }

                    List<Clinic> clinics = null;
                    Array clinicsArray = resultSet.getArray("clinics");
                    if(clinicsArray != null) {
                        Integer[] clinicsIds = (Integer[]) clinicsArray.getArray();
                        clinics = Arrays.stream(clinicsIds)
                                .map(i -> {
                                    Clinic clinic = new Clinic();
                                    try {
                                        PreparedStatement statementClinic = connection.prepareStatement(
                                                "SELECT name_clinic, address, type_clinic " +
                                                        "FROM clinic WHERE id = ?");
                                        statementClinic.setLong(1, i);
                                        ResultSet resultSetClinic = statementClinic.executeQuery();
                                        if(resultSetClinic.next()) {
                                            clinic.setId(Long.valueOf(i));
                                            clinic.setName(resultSetClinic.getString("name_clinic"));
                                            clinic.setAddress(resultSetClinic.getString("address"));
                                            clinic.setType(resultSetClinic.getString("type_clinic"));
                                        }
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                                return clinic;
                                })
                                .toList();
                    }

                    patient.setId(id);
                    patient.setLastName(resultSet.getString("lastname"));
                    patient.setFirstName(resultSet.getString("firstname"));
                    patient.setPatronymic(resultSet.getString("patronymic"));
                    patient.setJob(resultSet.getString("job"));
                    patient.setDoctors(doctors);
                    patient.setClinics(clinics);
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
                    "job, doctors, clinics FROM patient");
            ResultSet resultSet = statement.executeQuery();
            List<Patient> patients = new ArrayList<>();
            while (resultSet.next()) {
                Patient patient = new Patient();

                List<Doctor> doctors = null;
                Array doctorsArray = resultSet.getArray("doctors");
                if (doctorsArray != null) {
                    Integer[] doctorIds = (Integer[]) doctorsArray.getArray();
                    doctors = Arrays.stream(doctorIds)
                            .map(i -> {
                                Doctor doctor = new Doctor();
                                try {
                                    PreparedStatement statementDoctor = connection.prepareStatement(
                                            "SELECT lastname, firstname, patronymic, specialization " +
                                                    "FROM doctor WHERE id = ?");
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
                }

                List<Clinic> clinics = null;
                Array clinicsArray = resultSet.getArray("clinics");
                if (clinicsArray != null) {
                    Integer[] clinicsIds = (Integer[]) clinicsArray.getArray();
                    clinics = Arrays.stream(clinicsIds)
                            .map(i -> {
                                Clinic clinic = new Clinic();
                                try {
                                    PreparedStatement statementClinic = connection.prepareStatement(
                                            "SELECT name_clinic, address, type_clinic " +
                                                    "FROM clinic WHERE id = ?");
                                    statementClinic.setLong(1, i);
                                    ResultSet resultSetClinic = statementClinic.executeQuery();
                                    if (resultSetClinic.next()) {
                                        clinic.setId(Long.valueOf(i));
                                        clinic.setName(resultSetClinic.getString("name_clinic"));
                                        clinic.setAddress(resultSetClinic.getString("address"));
                                        clinic.setType(resultSetClinic.getString("type_clinic"));
                                    }
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                return clinic;
                            })
                            .toList();
                }

                patient.setId(resultSet.getLong("id"));
                patient.setLastName(resultSet.getString("lastname"));
                patient.setFirstName(resultSet.getString("firstname"));
                patient.setPatronymic(resultSet.getString("patronymic"));
                patient.setJob(resultSet.getString("job"));
                patient.setDoctors(doctors);
                patient.setClinics(clinics);

                patients.add(patient);
            }
            return patients;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
                Array doctorIds = null;
                if(patient.getDoctors() != null) {
                    List<Long> idDoctors = patient.getDoctors()
                            .stream()
                            .map(People::getId)
                            .toList();
                    doctorIds = connection.createArrayOf("int4", idDoctors.toArray());
                }

                Array clinicIds = null;
                if(patient.getClinics() != null) {
                    List<Long> idClinics = patient.getClinics()
                            .stream()
                            .map(Clinic::getId)
                            .toList();
                    clinicIds = connection.createArrayOf("int4", idClinics.toArray());
                }

                PreparedStatement statement = connection.prepareStatement("INSERT INTO patient (lastname, firstname, " +
                        "patronymic, job, doctors, clinics) " +
                        "VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, patient.getLastName());
                statement.setString(2, patient.getFirstName());
                statement.setString(3, patient.getPatronymic());
                statement.setString(4, patient.getJob());
                statement.setArray(5, doctorIds);
                statement.setArray(6, clinicIds);
                statement.executeUpdate();

                ResultSet resultSet = statement.getGeneratedKeys();
                if(resultSet.next()) {
                    patient.setId((resultSet.getLong(1)));
                }
            }
        }
    }

    /**
     * The method changes the entity Patient in the database.
     * @param patient entity Patient.
     */
    @Override
    public void update(Patient patient) {
        try (Connection connection = source.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE patient SET lastname=?, firstname=?, " +
                    "patronymic=?, job=?, doctors=?, clinics=? WHERE id=?");

            Array doctorIds = null;
            if(patient.getDoctors() != null){
                List<Long> idDoctors = patient.getDoctors()
                        .stream()
                        .map(People::getId)
                        .toList();
                doctorIds = connection.createArrayOf("int4", idDoctors.toArray());
            }

            Array clinicIds = null;
            if(patient.getClinics() != null) {

                List<Long> idClinics = patient.getClinics()
                        .stream()
                        .map(Clinic::getId)
                        .toList();
                clinicIds = connection.createArrayOf("int4", idClinics.toArray());
            }

            statement.setString(1, patient.getLastName());
            statement.setString(2, patient.getFirstName());
            statement.setString(3, patient.getPatronymic());
            statement.setString(4, patient.getJob());
            statement.setArray(5, doctorIds);
            statement.setArray(6, clinicIds);
            statement.setLong(7, patient.getId());

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
        return true;
    }
}
