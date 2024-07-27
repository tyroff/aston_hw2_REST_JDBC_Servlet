package model;

import java.util.List;
import java.util.Objects;

public class Patient extends People {
    private String job;
    private List<Clinic> clinics;
    private List<Doctor> doctors;

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public List<Clinic> getClinics() {
        return clinics;
    }

    public void setClinics(List<Clinic> clinics) {
        this.clinics = clinics;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Patient patient = (Patient) o;
        return Objects.equals(job, patient.job) && Objects.equals(clinics, patient.clinics) && Objects.equals(doctors, patient.doctors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), job, clinics, doctors);
    }

    @Override
    public String toString() {
        return "Patient" + super.toString() +
                ", job='" + job +
                ", doctor=" + doctors +
                ", clinic=" + clinics +
                "} ";
    }

}
