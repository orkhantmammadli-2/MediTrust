package com.ltc.sharedevents.dto;

public record AppointmentVerifiedEvent(
        Long appointmentId,
        Long patientId,
        String doctorName,
        String appointmentPlace,
        String complaintType,
        String feedback,
        String likedAspect1,
        String likedAspect2

) {
}
