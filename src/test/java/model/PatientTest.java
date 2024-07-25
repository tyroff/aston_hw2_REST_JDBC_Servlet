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
    void getJobTest() {
        patientTest.setJob("Job");
        assertEquals(patientTest.getJob(), "Job");
    }

    @Test
    void setJobTest() {
        patientTest.setJob("Job");
        assertEquals(patientTest.getJob(), "Job");
    }

    @Test
    void testEqualsTest() {
        assertTrue(patient.equals(patientEquals));
    }

    @Test
    void testHashCodeTest() {
        assertEquals(patient.hashCode(), hashCodePatient);
    }

    @Test
    void testToStringTest() {
        assertEquals(patient.toString(), toStringPatient);
    }
}