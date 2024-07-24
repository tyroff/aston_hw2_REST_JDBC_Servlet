package model;

import java.util.List;
import java.util.Objects;

public class Patient extends People {
    private String job;

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Patient patient = (Patient) o;
        return Objects.equals(job, patient.job);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), job);
    }

    @Override
    public String toString() {
        return "Patient" + super.toString() +
                ", job='" + job + '\'' +
                "} ";
    }
}
