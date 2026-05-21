package com.ltc.appointmentservice.service;

import com.ltc.appointmentservice.dto.AppointmentRequest;
import com.ltc.appointmentservice.dto.AppointmentResponse;
import com.ltc.appointmentservice.dto.PatientResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AppointmentService {
    AppointmentResponse addAppointment (AppointmentRequest request, MultipartFile file);
    AppointmentResponse getById (Long id);
    List<AppointmentResponse> getAll();
    List<AppointmentResponse> getByPatientId (Long patientId);
    AppointmentResponse updateAppointment (Long id, AppointmentRequest request);
    void deleteById (Long id);
    public void verifyAppointment(Long id);
}
