package service;

import dao.DoctorDaoImp;
import dto.DoctorDTO;
import mapper.DoctorMapper;
import model.Doctor;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The logical layer of the Doctor entity, which receives and processes data received from the server and database.
 */
public class DoctorService {
    private final DoctorDaoImp doctorDaoImp;

    /**
     * Constructor with the entity parameter DoctorDaoImp.
     */
    public DoctorService(DataSource source) {
        this.doctorDaoImp = new DoctorDaoImp(source);
    }


    /**
     * Returns one entity DoctorDTO by id otherwise null.
     * @param id id of entity Doctor.
     * @return DoctorDTO.
     */
    public DoctorDTO getById(Long id) {
        Doctor doctor = doctorDaoImp.getById(id);
        return DoctorMapper.INSTANCE.doctorToDTO(doctor);
    }

    /**
     * Returns a list of all DoctorDTO otherwise null.
     * @return List<DoctorDTO>
     */
    public List<DoctorDTO> getAll() {
        List<Doctor> doctors = doctorDaoImp.getAll();
        List<DoctorDTO> doctorDTOs = doctors
                .stream()
                .map(DoctorMapper.INSTANCE::doctorToDTO)
                .collect(Collectors.toList());
        return doctorDTOs;
    }

    /**
     * The method receives from the controller the entity DoctorDTO and saves in the database the entity Doctor.
     * @param doctorDTO the entity DoctorDTO.
     */
    public void save(DoctorDTO doctorDTO) throws SQLException {
        Doctor doctor = DoctorMapper.INSTANCE.DTOtoDoctor(doctorDTO);
        doctorDaoImp.save(doctor);
    }

    /**
     * The method receives from the controller the entity DoctorDTO and changes the entity Doctor in the database.
     * @param doctorDTO the entity DoctorDTO.
     */
    public void update(DoctorDTO doctorDTO) throws SQLException {
        Doctor doctor = DoctorMapper.INSTANCE.DTOtoDoctor(doctorDTO);
        doctorDaoImp.update(doctor);
    }

    /**
     * The method gets from the id of DoctorDTO entity controller and removes the id Doctor entity in the database.
     * @param id the id Doctor entity
     * @return true if the id Doctor entity was deleted from the database otherwise false.
     */
    public Boolean deleteById(Long id) {
        return doctorDaoImp.deleteById(id);
    }
}
