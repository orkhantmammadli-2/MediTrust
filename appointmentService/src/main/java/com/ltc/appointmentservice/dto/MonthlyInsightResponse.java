package com.ltc.appointmentservice.dto;

public record MonthlyInsightResponse(

        String topComplaintType,
        Long topComplaintCount,

        String topHospital,
        Long topHospitalVisitCount,

        String aiSummary

) {
}
