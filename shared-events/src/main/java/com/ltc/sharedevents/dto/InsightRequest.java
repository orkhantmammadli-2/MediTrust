package com.ltc.sharedevents.dto;

public record InsightRequest (
     String topComplaintType,
     Long topComplaintCount,
     String topHospital,
     Long topHospitalVisitCount
) {}
