package com.ltc.patientservice.exception;

public class PatientAlreadyExists extends RuntimeException {
    public PatientAlreadyExists(String message) {
        super(message);
    }
}
