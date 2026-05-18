package com.ltc.appointmentservice.service;

import com.ltc.appointmentservice.dto.AppointmentRequest;
import com.ltc.appointmentservice.dto.AppointmentResponse;
import com.ltc.appointmentservice.dto.PatientResponse;
import com.ltc.appointmentservice.entity.Appointment;
import com.ltc.appointmentservice.exception.AppointmentNotFound;
import com.ltc.appointmentservice.feign.PatientClient;
import com.ltc.appointmentservice.mapper.AppointmentMapper;
import com.ltc.appointmentservice.repository.AppointmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
@Slf4j
public class AppointmentServiceImpl implements AppointmentService{
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final PatientClient patientClient;
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, AppointmentMapper appointmentMapper, PatientClient patientClient) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.patientClient = patientClient;
    }


    @Override
    public AppointmentResponse addAppointment(AppointmentRequest request) {
        patientClient.getPatientById(request.getPatientId());
        Appointment appointment = appointmentMapper.toEntity(request);
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
