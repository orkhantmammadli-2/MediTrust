package com.ltc.patientservice.controller;


import com.ltc.patientservice.dto.PatientRequest;
import com.ltc.patientservice.dto.PatientResponse;
import com.ltc.patientservice.service.PatientServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
@Slf4j

public class PatientController {
    private final PatientServiceImpl patientServiceImple;

    public PatientController(PatientServiceImpl patientServiceImple) {
        this.patientServiceImple = patientServiceImple;
    }
    @Operation(summary = "Add a new patient")
    @PostMapping("/addPatient")
    public ResponseEntity<PatientResponse> addPatient(@Valid @RequestBody PatientRequest patientRequest) {
        PatientResponse patientResponse = patientServiceImple.create(patientRequest);
        log.info("Patient added : {}", patientResponse);
        return new ResponseEntity<>(patientResponse,HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientServiceImple.delete(id);
        log.info("Patient deleted : {}", id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
