package service;

import dao.PatientDaoImp;
import dto.PatientDTO;
import mapper.PatientMapper;
import model.Patient;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The logical layer of the Patient entity, which receives and processes data received from the server and database.
 */
public class PatientService {
    private final PatientDaoImp patientDaoImp;

    /**
     * Constructor with the entity parameter PatientDaoImp.
     * @param patientDaoImp entity PatientDaoImp.
     */
    public PatientService(PatientDaoImp patientDaoImp) {
        this.patientDaoImp = patientDaoImp;
    }

    /**
     * Returns one entity PatientDTO by id otherwise null.
     * @param id id of entity Patient.
     * @return PatientDTO.
     */
    public PatientDTO getById(Long id) {
        Patient patient = patientDaoImp.getById(id);
        return PatientMapper.INSTANCE.patientToDTO(patient);
    }

    /**
     * Returns a list of all PatientDTO otherwise null.
     * @return List<PatientDTO>
     */
    public List<PatientDTO> getAll() {
        List<Patient> patients = patientDaoImp.getAll();
        List<PatientDTO> patientDTOs = patients
                .stream()
                .map(PatientMapper.INSTANCE::patientToDTO)
                .collect(Collectors.toList());
        return patientDTOs;
    }

    /**
     * The method receives from the controller the entity PatientDTO and saves in the database the entity Patient.
     * @param patientDTO the entity PatientDTO.
     */
    public void save(PatientDTO patientDTO) throws SQLException {
        Patient patient = PatientMapper.INSTANCE.DTOtoPatient(patientDTO);
        patientDaoImp.save(patient);
    }

    /**
     * The method receives from the controller the entity PatientDTO and changes the entity Patient in the database.
     * @param patientDTO the entity PatientDTO.
     */
    public void update(PatientDTO patientDTO) throws SQLException {
        Patient patient = PatientMapper.INSTANCE.DTOtoPatient(patientDTO);
        patientDaoImp.update(patient);
    }

    /**
     * The method gets from the id of PatientDTO entity controller and removes the id Patient entity in the database.
     * @param id the id Patient entity
     * @return true if the id Patient entity was deleted from the database otherwise false.
     */
    public Boolean deleteById(Long id) {
        return patientDaoImp.deleteById(id);
    }
}
