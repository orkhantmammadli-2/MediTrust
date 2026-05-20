package com.ltc.appointmentservice.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ltc.appointmentservice.dto.AppointmentRequest;
import com.ltc.appointmentservice.dto.AppointmentResponse;
import com.ltc.appointmentservice.service.AppointmentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AppointmentResponse>
    addAppointment( @RequestPart("data") String requestJson,
                    @RequestPart("file") MultipartFile file)
            throws JsonProcessingException { ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule( new JavaTimeModule());
        AppointmentRequest request = mapper.readValue( requestJson, AppointmentRequest.class);
        AppointmentResponse response = appointmentServiceImpl.addAppointment( request, file);
        return new ResponseEntity<>( response, HttpStatus.CREATED);}

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
