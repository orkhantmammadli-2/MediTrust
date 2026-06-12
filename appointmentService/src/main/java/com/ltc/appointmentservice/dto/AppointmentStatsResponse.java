package com.ltc.appointmentservice.dto;

import java.io.Serializable;

public record AppointmentStatsResponse(
        long totalAppointments,
        long verifiedAppointments,
        long pendingAppointments
) implements Serializable {}