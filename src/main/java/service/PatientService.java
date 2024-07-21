package service;

import dao.PatientDaoImp;
import dto.PatientDTO;
import mapper.PatientMapper;
import model.Patient;

import java.util.Optional;

public class PatientService {
    private final PatientDaoImp patientDaoImp;

    public PatientService(PatientDaoImp patientDaoImp) {
        this.patientDaoImp = patientDaoImp;
    }

    public PatientDTO getPatientById(Long id) {
        Optional<Patient> patient = patientDaoImp.getById(id);
        return PatientMapper.INSTANCE.patientToDTO(patient);
    }
}
