package com.ltc.appointmentservice.dto;


import com.ltc.appointmentservice.entity.Rating;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class AppointmentResponse {
    private Long id;

    private Long patientId;

    private LocalDateTime appointmentDate;

    private String appointmentPlace;

    private String doctorName;

    private String complaintType;

    private Rating rating;

    private String feedback;

    private String likedAspect1;

    private String likedAspect2;

    private String admissionDocumentPath;

    private boolean admissionVerified;
}
