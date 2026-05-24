package com.ltc.notificationservice.kafka;

import com.ltc.sharedevents.dto.AppointmentCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AppointmentConsumer {
    @KafkaListener(
            topics = "appointment-created",
            groupId = "notification-group")

    public void consume(
            AppointmentCreatedEvent appointmentCreatedEvent) {
        System.out.println( "EVENT RECEIVED: " + appointmentCreatedEvent);
    }
}