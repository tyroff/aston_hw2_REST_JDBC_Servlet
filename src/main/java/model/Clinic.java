package model;

import java.util.List;
import java.util.Objects;

public class Clinic extends Building {
    private String type;
    private List<Doctor> doctors;
    private List<Patient> patients;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Clinic clinic = (Clinic) o;
        return Objects.equals(type, clinic.type) && Objects.equals(doctors, clinic.doctors) && Objects.equals(patients, clinic.patients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type, doctors, patients);
    }

    @Override
    public String toString() {
        return "Clinic{" +
                "type='" + type + '\'' +
                ", doctors=" + doctors +
                ", patients=" + patients +
                '}';
    }
}
