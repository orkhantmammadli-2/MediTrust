package com.ltc.appointmentservice.controller;


import com.ltc.appointmentservice.dto.AppointmentRequest;
import com.ltc.appointmentservice.dto.AppointmentResponse;
import com.ltc.appointmentservice.dto.PatientResponse;
import com.ltc.appointmentservice.service.AppointmentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@Slf4j
public class AppointmentController {
    private final AppointmentServiceImpl appointmentServiceImpl;
    public AppointmentController(AppointmentServiceImpl appointmentServiceImpl) {
        this.appointmentServiceImpl = appointmentServiceImpl;
    }
    @Operation(summary = "Add a new appointment")
    @PostMapping("/add")
    public ResponseEntity<AppointmentResponse> addAppointment(@Valid @RequestBody AppointmentRequest request){
        AppointmentResponse appointmentResponse = appointmentServiceImpl.addAppointment(request);
        return new ResponseEntity<>(appointmentResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Show all appointments")
    @GetMapping("/all")
    public ResponseEntity<List<AppointmentResponse>> allAppointments() {
    List<AppointmentResponse> appointmentResponse = appointmentServiceImpl.getAll();
    log.info("All appointments successfully");
    return new ResponseEntity<>(appointmentResponse, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Get appointment by ID")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Long id){
        AppointmentResponse appointmentResponse = appointmentServiceImpl.getById(id);
        log.info("Get appointment by ID successfully");
        return new ResponseEntity<>(appointmentResponse, HttpStatus.OK);
    }

    @Operation(summary = "Delete appointment by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointmentById(@PathVariable Long id){
        appointmentServiceImpl.deleteById(id);
        log.info("Delete appointment by ID successfully");
        return new ResponseEntity<>("Deleted successfully",HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Updating appointment by Id")
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponse> updateAppointmentById(@PathVariable Long id, @Valid @RequestBody AppointmentRequest request){
        AppointmentResponse appointmentResponse = appointmentServiceImpl.updateAppointment(id, request);
        log.info("Update appointment by ID successfully");
        return new ResponseEntity<>(appointmentResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get all Appointments by Patient Id")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> getByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(
                appointmentServiceImpl.getByPatientId(
                        patientId
                )
        );
    }
}
