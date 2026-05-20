package com.ltc.appointmentservice.dto;

import com.ltc.appointmentservice.entity.Rating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentRequest {
    @NotNull
    private Long patientId;
    @NotNull
    private LocalDateTime appointmentDate;
    @NotBlank @Size(min = 1, max = 20)
    private String appointmentPlace;
    @NotBlank
    private String doctorName;
    @NotBlank @Size(min = 1, max = 15, message = "Qısa formada")
    private String complaintType;
    @NotNull
    private Rating rating;
    @NotBlank @Size(min = 10, max = 100)
    private String feedback;
    @NotBlank @Size(min = 3, max = 20, message = "1 sözlə ifadə edin")
    private String likedAspect1;
    @NotBlank @Size(min = 3, max = 20, message = "1 sözlə ifadə edin")
    private String likedAspect2;
}
