package com.ltc.appointmentservice.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Appointments")

public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long patientId;
    @Column(nullable = false)
    private LocalDateTime appointmentDate;
    @Column(nullable = false)
    private String appointmentPlace;
    @Column(nullable = false)
    private String doctorName;
    @Column(nullable = false)
    private String complaintType;
    @Enumerated(EnumType.STRING)
    private Rating rating;
    @Column(nullable = false, length = 100)
    private String feedback;
    @Column(nullable = false)
    private String likedAspect1;
    @Column(nullable = false)
    private String likedAspect2;
    private String admissionDocumentPath;
    private boolean admissionVerified;
}
