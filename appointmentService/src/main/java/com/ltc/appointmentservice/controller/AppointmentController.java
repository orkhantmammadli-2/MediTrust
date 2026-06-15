package com.ltc.appointmentservice.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ltc.appointmentservice.dto.AppointmentRequest;
import com.ltc.appointmentservice.dto.AppointmentResponse;
import com.ltc.appointmentservice.dto.AppointmentStatsResponse;
import com.ltc.appointmentservice.dto.MonthlyInsightResponse;
import com.ltc.appointmentservice.service.AppointmentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@Slf4j
@CrossOrigin(origins = "*")

public class AppointmentController {
    private final AppointmentServiceImpl appointmentServiceImpl;
    public AppointmentController(AppointmentServiceImpl appointmentServiceImpl) {
        this.appointmentServiceImpl = appointmentServiceImpl;
    }
    @Operation(summary = "Add a new appointment")
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<AppointmentResponse>
    addAppointment( @RequestPart("data") String requestJson,
                    @RequestPart(value = "file", required = false) MultipartFile file)
            throws JsonProcessingException { ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule( new JavaTimeModule());
        AppointmentRequest request = mapper.readValue( requestJson, AppointmentRequest.class);
        AppointmentResponse response = appointmentServiceImpl.addAppointment( request, file);
        return new ResponseEntity<>( response, HttpStatus.CREATED);}

    @Operation(summary = "Show all appointments")
    @GetMapping("/all")
    public ResponseEntity<Page<AppointmentResponse>> allAppointments(@ParameterObject @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
    Page<AppointmentResponse> appointmentResponse = appointmentServiceImpl.getAll(pageable);
    log.info("All appointments successfully");
    return new ResponseEntity<>(appointmentResponse, HttpStatus.ACCEPTED);
    }
    @Operation(summary = "Showing verified")
    @GetMapping("/verified")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Page<AppointmentResponse>> getVerified(
            @ParameterObject
            @PageableDefault(page = 0, size = 5, sort = "id")
            Pageable pageable){ return ResponseEntity.ok(
                appointmentServiceImpl.getVerified(pageable));}

    @Operation(summary = "Get appointment by ID")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Long id){
        AppointmentResponse appointmentResponse = appointmentServiceImpl.getById(id);
        log.info("Get appointment by ID successfully");
        return new ResponseEntity<>(appointmentResponse, HttpStatus.OK);
    }

    @Operation(summary = "Delete appointment by ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteAppointmentById(@PathVariable Long id){
        appointmentServiceImpl.deleteById(id);
        log.info("Delete appointment by ID successfully");
        return new ResponseEntity<>("Deleted successfully",HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Updating appointment by Id")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<AppointmentResponse> updateAppointmentById(@PathVariable Long id, @RequestPart("data") String requestJson,
                                                                     @RequestPart(value = "file", required = false)
                                                                         MultipartFile file) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();mapper.registerModule(new JavaTimeModule());
        AppointmentRequest request = mapper.readValue(requestJson, AppointmentRequest.class);
        AppointmentResponse response = appointmentServiceImpl.updateAppointment(id, request, file);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all Appointments by Patient Id")
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<List<AppointmentResponse>> getByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(
                appointmentServiceImpl.getByPatientId(
                        patientId
                )
        );
    }
    @PatchMapping("/{id}/verify")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> verifyAppointment( @PathVariable Long id) {
        appointmentServiceImpl.verifyAppointment(id);
        return ResponseEntity.ok("Appointment verified successfully");}

    @GetMapping("/stats")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<AppointmentStatsResponse>
    getStats() {return ResponseEntity.ok(appointmentServiceImpl.getStats());}

    @GetMapping("/insights/monthly")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MonthlyInsightResponse> getMonthlyInsights() {
        return ResponseEntity.ok(appointmentServiceImpl.getMonthlyInsights());
    }
}
