package model;

import java.util.List;
import java.util.Objects;

public class Patient extends People {
    private String job;
    private List<Doctor> doctors;
    private List<Clinic> clinics;

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
        if (!super.equals(o)) return false;
        Patient patient = (Patient) o;
        return Objects.equals(job, patient.job) && Objects.equals(doctors, patient.doctors) && Objects.equals(clinics, patient.clinics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), job, doctors, clinics);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "job='" + job + '\'' +
                ", doctors=" + doctors +
                ", clinics=" + clinics +
                '}';
    }
}
