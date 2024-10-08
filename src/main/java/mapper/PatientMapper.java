package mapper;

import dto.PatientDTO;
import model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PatientMapper {
    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);
    PatientDTO patientToDTO(Patient patient);
    Patient DTOtoPatient(PatientDTO patientDTO);
    List<PatientDTO> patientsToDTOs(List<Patient> patients);
}
