package com.ltc.appointmentservice.service;

import com.ltc.appointmentservice.configuration.MultipartInputStreamFileResource;
import com.ltc.appointmentservice.dto.*;
import com.ltc.appointmentservice.entity.Appointment;
import com.ltc.appointmentservice.exception.AppointmentNotFound;
import com.ltc.appointmentservice.feign.AiClient;
import com.ltc.appointmentservice.feign.PatientClient;
import com.ltc.appointmentservice.mapper.AppointmentMapper;
import com.ltc.appointmentservice.repository.AppointmentRepository;
import com.ltc.sharedevents.dto.AppointmentVerifiedEvent;
import com.ltc.sharedevents.dto.InsightRequest;
import com.ltc.sharedevents.dto.InsightResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.ltc.sharedevents.dto.AppointmentCreatedEvent;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@Service
@Slf4j
public class AppointmentServiceImpl implements AppointmentService{
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final PatientClient patientClient;
    private final AiClient aiClient;
    private final RestTemplate restTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private final KafkaProducerService kafkaProducerService;
    private final static String APPOINT_CACHE_NAME = "appointCache";
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, AppointmentMapper appointmentMapper, PatientClient patientClient, AiClient aiClient, RestTemplate restTemplate, SimpMessagingTemplate messagingTemplate, KafkaProducerService kafkaProducerService) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.patientClient = patientClient;
        this.aiClient = aiClient;
        this.restTemplate = restTemplate;
        this.messagingTemplate = messagingTemplate;
        this.kafkaProducerService = kafkaProducerService;
    }


    @Override
    public AppointmentResponse addAppointment(AppointmentRequest request, MultipartFile file) {
        patientClient.getPatientById(request.getPatientId());
        Appointment appointment = appointmentMapper.toEntity(request);
        if (file != null && !file.isEmpty()) { MultiValueMap<String, Object> body =
                    new LinkedMultiValueMap<>();
            try { body.add( "file", new MultipartInputStreamFileResource(
                                file.getInputStream(),
                                file.getOriginalFilename()));
            } catch (IOException e) {throw new RuntimeException(e);}
            HttpHeaders headers = new HttpHeaders();headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<FileUploadResponse> uploadResponse = restTemplate.postForEntity(
                            "http://localhost:8088/api/v1/files/upload", requestEntity, FileUploadResponse.class);
            appointment.setAdmissionDocumentPath(uploadResponse.getBody().getFileUrl());}
        appointment.setAdmissionVerified(false);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        AppointmentResponse response = appointmentMapper.toResponse(savedAppointment);
        messagingTemplate.convertAndSend("/topic/appointments", response);
        kafkaProducerService.sendAppointmentCreatedEvent(
                new AppointmentCreatedEvent(
                        savedAppointment.getId(),
                        savedAppointment.getPatientId(),
                        savedAppointment.getDoctorName(),
                        savedAppointment.getAppointmentPlace(),
                        savedAppointment.getComplaintType(),
                        savedAppointment.getFeedback(),
                        savedAppointment.getLikedAspect1(),
                        savedAppointment.getLikedAspect2()));
        return response;
    }

    @Override
    @Cacheable(value = "APPOINT_CACHE_NAME", key = "#id")
    public AppointmentResponse getById(Long id) {
        Appointment appointment = appointmentRepository
                .findById(id).orElseThrow(()-> new AppointmentNotFound
                        ("Appointment not found with id " + id));
        return appointmentMapper.toResponse(appointment);
    }

    @Override
    public Page<AppointmentResponse> getAll(Pageable pageable) {
        return appointmentRepository.findAll(pageable)
                .map(appointmentMapper::toResponse);
    }

    @Override
    public Page<AppointmentResponse> getVerified(Pageable pageable) {
        return appointmentRepository.findByAdmissionVerifiedTrue(pageable)
                .map(appointmentMapper::toResponse);
    }

    @Override
    @Cacheable(value = "APPOINT_CACHE_NAME", key = "#patientId")
    public List<AppointmentResponse> getByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .map(appointmentMapper::toResponse)
                .toList();}

    @Override
    public AppointmentResponse updateAppointment(Long id, AppointmentRequest request, MultipartFile file) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(()-> new AppointmentNotFound("Appointment not found with id " + id));
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentPlace(request.getAppointmentPlace());
        appointment.setDoctorName(request.getDoctorName());
        appointment.setAdmissionVerified(false);
        appointment.setComplaintType(request.getComplaintType());
        appointment.setRating(request.getRating());
        appointment.setFeedback(request.getFeedback());
        appointment.setLikedAspect1(request.getLikedAspect1());
        appointment.setLikedAspect2(request.getLikedAspect2());
        if (file != null && !file.isEmpty()) { MultiValueMap<String, Object> body =
                    new LinkedMultiValueMap<>(); try { body.add("file",
                        new MultipartInputStreamFileResource(
                                file.getInputStream(),
                                file.getOriginalFilename()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>>
                    requestEntity =
                    new HttpEntity<>(body, headers);
            ResponseEntity<FileUploadResponse> uploadResponse = restTemplate.postForEntity(
                            "http://localhost:8088/api/v1/files/upload",
                            requestEntity,
                            FileUploadResponse.class);
            appointment.setAdmissionDocumentPath(
                    uploadResponse.getBody().getFileUrl());}
        Appointment updated = appointmentRepository.save(appointment);
        return appointmentMapper.toResponse(updated);}

    @Override
    @CacheEvict(value = "APPOINT_CACHE_NAME", key = "#id")
    public void deleteById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(()-> new AppointmentNotFound("Appointment not found with id " + id));
        appointmentRepository.delete(appointment);
    }
    @Override
    public void verifyAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() ->
                                new AppointmentNotFound("Not found"));
        appointment.setAdmissionVerified(true);
        appointmentRepository.save(appointment);
        messagingTemplate.convertAndSend("/topic/appointments",
                new NotificationMessage("APPOINTMENT_VERIFIED"));
        kafkaProducerService.sendAppointmentVerifiedEvent(
                new AppointmentVerifiedEvent(
                        appointment.getId(),
                        appointment.getPatientId(),
                        appointment.getDoctorName(),
                        appointment.getAppointmentPlace(),
                        appointment.getComplaintType(),
                        appointment.getFeedback(),
                        appointment.getLikedAspect1(),
                        appointment.getLikedAspect2()));
    }

    public AppointmentStatsResponse getStats() {
        long total = appointmentRepository.count();
        long verified = appointmentRepository
                        .findAll()
                        .stream()
                        .filter(
                                Appointment::isAdmissionVerified
                        )
                        .count();
        long pending = total - verified;
        return new AppointmentStatsResponse(
                total,
                verified,
                pending
        );
    }
    public MonthlyInsightResponse
    getMonthlyInsights() {LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        List<Object[]> complaints = appointmentRepository.findTopComplaintType();
        System.out.println("COMPLAINTS = " + complaints);
        List<Object[]> hospitals = appointmentRepository.findTopHospital();

        System.out.println("HOSPITALS = " + hospitals);
        Object[] complaint = complaints.get(0);
        Object[] hospital = hospitals.get(0);
        Long complaintCount =
                ((Number) complaint[1])
                        .longValue();

        Long hospitalCount =
                ((Number) hospital[1])
                        .longValue();
        InsightRequest request =
                new InsightRequest(
                        complaint[0].toString(),
                        complaintCount,
                        hospital[0].toString(),
                        hospitalCount
                );
        InsightResponse aiResponse = aiClient.generateInsight(request);
        return new MonthlyInsightResponse(
                complaint[0].toString(),
                complaintCount,
                hospital[0].toString(),
                hospitalCount,
                aiResponse.summary()
        );
    }
}
