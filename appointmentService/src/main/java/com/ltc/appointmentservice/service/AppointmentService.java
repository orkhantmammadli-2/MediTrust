package com.ltc.appointmentservice.service;

import com.ltc.appointmentservice.dto.AppointmentRequest;
import com.ltc.appointmentservice.dto.AppointmentResponse;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AppointmentService {
    AppointmentResponse addAppointment (AppointmentRequest request, MultipartFile file);
    AppointmentResponse getById (Long id);
    Page<AppointmentResponse> getAll(Pageable pageable);
    Page<AppointmentResponse> getVerified(Pageable pageable);
    List<AppointmentResponse> getByPatientId (Long patientId);
    AppointmentResponse updateAppointment (Long id, AppointmentRequest request, MultipartFile file);
    void deleteById (Long id);
    public void verifyAppointment(Long id);
}
