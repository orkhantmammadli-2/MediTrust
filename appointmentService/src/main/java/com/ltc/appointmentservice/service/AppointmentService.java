package com.ltc.appointmentservice.service;

import com.ltc.appointmentservice.dto.AppointmentRequest;
import com.ltc.appointmentservice.dto.AppointmentResponse;
import com.ltc.appointmentservice.dto.PatientResponse;

import java.util.List;

public interface AppointmentService {
    AppointmentResponse addAppointment (AppointmentRequest request);
    AppointmentResponse getById (Long id);
    List<AppointmentResponse> getAll();
    List<AppointmentResponse> getByPatientId (Long patientId);
    AppointmentResponse updateAppointment (Long id, AppointmentRequest request);
    void deleteById (Long id);
}
