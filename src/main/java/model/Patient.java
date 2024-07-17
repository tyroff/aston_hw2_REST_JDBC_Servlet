package model;

import java.util.Objects;

public class Patient extends People {
    private String job;
    private Doctor doctor;

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Patient patient = (Patient) o;
        return Objects.equals(job, patient.job) && Objects.equals(doctor, patient.doctor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), job, doctor);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "job='" + job + '\'' +
                ", doctor=" + doctor +
                '}';
    }
}
