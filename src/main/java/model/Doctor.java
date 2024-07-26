package model;

import java.util.List;
import java.util.Objects;

public class Doctor extends People{
    private String specialization;
    private Clinic clinic;
    private List<Patient> patients;

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(specialization, doctor.specialization) && Objects.equals(clinic, doctor.clinic) && Objects.equals(patients, doctor.patients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), specialization, clinic, patients);
    }

    @Override
    public String toString() {
        return "Doctor" + super.toString() +
                "specialization='" + specialization + '\'' +
                ", clinic=" + clinic +
                ", patients=" + patients +
                "}";
    }
}
