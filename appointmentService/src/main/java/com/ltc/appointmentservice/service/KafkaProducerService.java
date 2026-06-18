package com.ltc.appointmentservice.service;

import com.ltc.sharedevents.dto.AppointmentCreatedEvent;
import com.ltc.sharedevents.dto.AppointmentVerifiedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public void sendAppointmentCreatedEvent(AppointmentCreatedEvent appointmentCreatedEvent) {
        kafkaTemplate.send("appointment-created-v2", appointmentCreatedEvent);
    }
    public void sendAppointmentVerifiedEvent(
            AppointmentVerifiedEvent appointmentVerifiedEvent) {
        kafkaTemplate.send("appointment-verified-v2", appointmentVerifiedEvent);
    }
}
