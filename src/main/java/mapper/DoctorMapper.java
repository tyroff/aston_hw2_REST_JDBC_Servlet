package mapper;

import dto.DoctorDTO;
import model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DoctorMapper {
    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);
    DoctorDTO doctorToDTO(Doctor doctor);
    Doctor DTOtoDoctor(DoctorDTO doctorDTO);
    List<DoctorDTO> doctorToDTOs(List<Doctor> doctors);
}
