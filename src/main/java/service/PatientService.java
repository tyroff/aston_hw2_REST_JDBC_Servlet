package service;

import dao.PatientDaoImp;
import dto.PatientDTO;
import mapper.PatientMapper;
import model.Patient;

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
     * @param patientDaoImp entity parameter PatientDaoImp.
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
     * @throws SQLException
     */
    public void save(PatientDTO patientDTO) throws SQLException {
        Patient patient = PatientMapper.INSTANCE.DTOtoPatient(patientDTO);
        patientDaoImp.save(patient);
    }
}

/*
        patientDaoImp.deleteById();
        patientDaoImp.update();
 */