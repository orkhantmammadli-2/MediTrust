package com.ltc.aiservice.dto;

public record InsightRequest (
        String topComplaintType,
        Long topComplaintCount,
        String topHospital,
        Long topHospitalVisitCount
) {}
