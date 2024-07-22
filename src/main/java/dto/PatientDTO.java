package dto;

import model.Clinic;
import model.Doctor;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PatientDTO {
    private Long id;
    private String lastName, firstName, patronymic;
    private Date birthday;
    private String job;
    private List<Doctor> doctors;
    private List<Clinic> clinics;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public List<Clinic> getClinics() {
        return clinics;
    }

    public void setClinics(List<Clinic> clinics) {
        this.clinics = clinics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientDTO that = (PatientDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(lastName, that.lastName) && Objects.equals(firstName, that.firstName) && Objects.equals(patronymic, that.patronymic) && Objects.equals(birthday, that.birthday) && Objects.equals(job, that.job) && Objects.equals(doctors, that.doctors) && Objects.equals(clinics, that.clinics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastName, firstName, patronymic, birthday, job, doctors, clinics);
    }

    @Override
    public String toString() {
        return "PatientDTO{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthday=" + birthday +
                ", job='" + job + '\'' +
                ", doctors=" + doctors +
                ", clinics=" + clinics +
                '}';
    }
}
