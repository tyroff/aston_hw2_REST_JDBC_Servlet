package mapper;

import dto.ClinicDTO;
import model.Clinic;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ClinicMapper {
    ClinicMapper INSTANCE = Mappers.getMapper(ClinicMapper.class);
    ClinicDTO clinicToDTO(Clinic clinic);
    Clinic DTOtoClinic(ClinicDTO clinicDTO);
    List<ClinicDTO> clinicToDTOs(List<Clinic> clinics);
}
