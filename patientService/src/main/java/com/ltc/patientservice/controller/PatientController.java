package com.ltc.patientservice.controller;


import com.ltc.patientservice.dto.PatientRequest;
import com.ltc.patientservice.dto.PatientResponse;
import com.ltc.patientservice.service.PatientServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@Slf4j

public class PatientController {
    private final PatientServiceImpl patientServiceImple;

    public PatientController(PatientServiceImpl patientServiceImple) {
        this.patientServiceImple = patientServiceImple;
    }
    @Operation(summary = "Add a new patient")
    @PostMapping("/addPatient")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<PatientResponse> addPatient(@Valid @RequestBody PatientRequest patientRequest) {
        PatientResponse patientResponse = patientServiceImple.create(patientRequest);
        log.info("Patient added : {}", patientResponse);
        return new ResponseEntity<>(patientResponse,HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(summary = "Delete a patient")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientServiceImple.delete(id);
        log.info("Patient deleted : {}", id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(summary = "Show all patients")
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        List<PatientResponse> patientResponses =  patientServiceImple.getAll();
        log.info("Patient list retrieved : {}", patientResponses);
        return new ResponseEntity<>(patientResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(summary = "Get patient by Id")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable Long id) {
        PatientResponse patientResponse = patientServiceImple.getById(id);
        log.info("Patient retrieved : {}", patientResponse);
        return new ResponseEntity<>(patientResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(summary = "Update patient by Id")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long id,
                                                         @Valid @RequestBody PatientRequest patientRequest) {
        PatientResponse patientResponse = patientServiceImple.update(id, patientRequest);
        log.info("Patient updated : {}", patientResponse);
        return new ResponseEntity<>(patientResponse, HttpStatus.ACCEPTED);
    }

}
