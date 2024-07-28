package service;

import dao.ClinicDaoImp;
import dto.ClinicDTO;
import mapper.ClinicMapper;
import model.Clinic;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The logical layer of the Clinic entity, which receives and processes data received from the server and database.
 */
public class ClinicService {
    private final ClinicDaoImp clinicDaoImp;

    /**
     * Constructor with the entity parameter ClinicDaoImp.
     */
    public ClinicService(ClinicDaoImp clinicDaoImp) {
        this.clinicDaoImp = clinicDaoImp;
    }


    /**
     * Returns one entity ClinicDTO by id otherwise null.
     * @param id id of entity Clinic.
     * @return ClinicDTO.
     */
    public ClinicDTO getById(Long id) {
        Clinic clinic = clinicDaoImp.getById(id);
        return ClinicMapper.INSTANCE.clinicToDTO(clinic);
    }

    /**
     * Returns a list of all ClinicDTO otherwise null.
     * @return List<ClinicDTO>
     */
    public List<ClinicDTO> getAll() {
        List<Clinic> clinics = clinicDaoImp.getAll();
        List<ClinicDTO> clinicDTOs = clinics
                .stream()
                .map(ClinicMapper.INSTANCE::clinicToDTO)
                .collect(Collectors.toList());
        return clinicDTOs;
    }

    /**
     * The method receives from the controller the entity ClinicDTO and saves in the database the entity Clinic.
     * @param clinicDTO the entity ClinicDTO.
     */
    public void save(ClinicDTO clinicDTO) throws SQLException {
        Clinic clinic = ClinicMapper.INSTANCE.DTOtoClinic(clinicDTO);
        clinicDaoImp.save(clinic);
    }

    /**
     * The method receives from the controller the entity ClinicDTO and changes the entity Clinic in the database.
     * @param clinicDTO the entity ClinicDTO.
     */
    public void update(ClinicDTO clinicDTO) throws SQLException {
        Clinic clinic = ClinicMapper.INSTANCE.DTOtoClinic(clinicDTO);
        clinicDaoImp.update(clinic);
    }

    /**
     * The method gets from the id of ClinicDTO entity controller and removes the id Clinic entity in the database.
     * @param id the id Clinic entity
     * @return true if the id Clinic entity was deleted from the database otherwise false.
     */
    public Boolean deleteById(Long id) {
        return clinicDaoImp.deleteById(id);
    }
}
