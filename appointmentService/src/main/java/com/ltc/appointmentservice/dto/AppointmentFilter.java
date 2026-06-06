package com.ltc.appointmentservice.dto;

public record AppointmentFilter(
        String doctorName,
        String complaintType,
        String appointmentPlace,
        String likedAspect1,
        String likedAspect2,
        String feedback,
        String rating,
        Boolean admissionVerified,
        Boolean hasDocument
) {
}
