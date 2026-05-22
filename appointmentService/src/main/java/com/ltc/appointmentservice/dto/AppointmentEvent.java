package com.ltc.appointmentservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentEvent {
    private String type; // CREATED / VERIFIED
    private Object data;
}