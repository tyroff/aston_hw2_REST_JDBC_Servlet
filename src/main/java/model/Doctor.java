package model;

import java.util.List;
import java.util.Objects;

public class Doctor extends People{
    private List<Patient> patients;

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
        Doctor doctor = (Doctor) o;
        return Objects.equals(patients, doctor.patients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), patients);
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "patients=" + patients +
                '}';
    }
}
