package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest extends People {
    Patient patient = new Patient();
    Patient patientTest = new Patient();
    Patient patientEquals = patient;
    Integer hashCodePatient = 28630112;
    String toStringPatient = "Patient{id=null, lastName='null', firstName='null', patronymic='null', job='null'} ";

    @Test
    void getJob() {
        patientTest.setJob("Job");
        assertEquals(patientTest.getJob(), "Job");
    }

    @Test
    void setJob() {
        patientTest.setJob("Job");
        assertEquals(patientTest.getJob(), "Job");
    }

    @Test
    void testEquals() {
        assertTrue(patient.equals(patientEquals));
    }

    @Test
    void testHashCode() {
        assertEquals(patient.hashCode(), hashCodePatient);
    }

    @Test
    void testToString() {
        assertEquals(patient.toString(), toStringPatient);
    }
}