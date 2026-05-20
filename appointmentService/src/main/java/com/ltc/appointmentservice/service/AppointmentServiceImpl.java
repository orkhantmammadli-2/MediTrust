package com.ltc.appointmentservice.service;

import com.ltc.appointmentservice.configuration.MultipartInputStreamFileResource;
import com.ltc.appointmentservice.dto.AppointmentRequest;
import com.ltc.appointmentservice.dto.AppointmentResponse;
import com.ltc.appointmentservice.dto.FileUploadResponse;
import com.ltc.appointmentservice.entity.Appointment;
import com.ltc.appointmentservice.exception.AppointmentNotFound;
import com.ltc.appointmentservice.feign.PatientClient;
import com.ltc.appointmentservice.mapper.AppointmentMapper;
import com.ltc.appointmentservice.repository.AppointmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
@Slf4j
public class AppointmentServiceImpl implements AppointmentService{
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final PatientClient patientClient;
    private final RestTemplate restTemplate;
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, AppointmentMapper appointmentMapper, PatientClient patientClient, RestTemplate restTemplate) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.patientClient = patientClient;
        this.restTemplate = restTemplate;
    }


    @Override
    public AppointmentResponse addAppointment(AppointmentRequest request, MultipartFile file) {
        patientClient.getPatientById(request.getPatientId());
        MultiValueMap<String, Object> body =
                new LinkedMultiValueMap<>();

        try {
            body.add(
                    "file",
                    new MultipartInputStreamFileResource(
                            file.getInputStream(),
                            file.getOriginalFilename()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                MediaType.MULTIPART_FORM_DATA
        );

        HttpEntity<MultiValueMap<String, Object>>
                requestEntity =
                new HttpEntity<>(body, headers);

        // Call file-service
        ResponseEntity<FileUploadResponse>
                uploadResponse =
                restTemplate.postForEntity(
                        "http://localhost:8088/api/v1/files/upload",
                        requestEntity,
                        FileUploadResponse.class
                );
        Appointment appointment = appointmentMapper.toEntity(request);
        appointment.setAdmissionDocumentPath(
                uploadResponse.getBody().getFileUrl()
        );
        appointment.setAdmissionVerified(false);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toResponse(savedAppointment);
    }

    @Override
    public AppointmentResponse getById(Long id) {
        Appointment appointment = appointmentRepository
                .findById(id).orElseThrow(()-> new AppointmentNotFound
                        ("Appointment not found with id " + id));
        return appointmentMapper.toResponse(appointment);
    }

    @Override
    public List<AppointmentResponse> getAll() {
        return appointmentRepository.findAll().stream()
                .map(appointmentMapper::toResponse).toList();
    }

    @Override
    public List<AppointmentResponse> getByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @Override
    public AppointmentResponse updateAppointment(Long id, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(()-> new AppointmentNotFound("Appointment not found with id " + id));
        appointmentMapper.updateAppointment(request, appointment);
        appointment.setAdmissionVerified(true);
        Appointment updated = appointmentRepository.save(appointment);
        return appointmentMapper.toResponse(updated);}

    @Override
    public void deleteById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(()-> new AppointmentNotFound("Appointment not found with id " + id));
        appointmentRepository.delete(appointment);
    }
}
