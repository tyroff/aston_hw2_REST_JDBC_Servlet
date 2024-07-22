package mapper;

import dto.PatientDTO;
import model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PatientMapper {
    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);
    PatientDTO patientToDTO(Patient patient);
    Patient DTOtoPatient(PatientDTO patientDTO);
    List<PatientDTO> patientsToDTOs(List<Patient> patients);

    //TODO: @Mapping(source = "doctor", target = "doctor") add
    //TODO: @Mapping(source = "clinic", target = "clinic") add
}
