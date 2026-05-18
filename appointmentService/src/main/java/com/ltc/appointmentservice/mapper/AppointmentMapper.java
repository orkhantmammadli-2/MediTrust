package com.ltc.appointmentservice.mapper;


import com.ltc.appointmentservice.dto.AppointmentRequest;
import com.ltc.appointmentservice.dto.AppointmentResponse;
import com.ltc.appointmentservice.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    Appointment toEntity (AppointmentRequest request);
    AppointmentResponse toResponse (Appointment appointment);
    void updateAppointment(AppointmentRequest request, @MappingTarget Appointment entity);
}

