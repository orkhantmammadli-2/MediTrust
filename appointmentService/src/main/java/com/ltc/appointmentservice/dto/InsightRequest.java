package com.ltc.appointmentservice.dto;

public record InsightRequest (
        String topComplaintType,
        Long topComplaintCount,
        String topHospital,
        Long topHospitalVisitCount
) {}

