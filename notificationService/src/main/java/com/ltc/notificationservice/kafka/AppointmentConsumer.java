package com.ltc.notificationservice.kafka;

import com.ltc.sharedevents.dto.AppointmentCreatedEvent;
import com.ltc.sharedevents.dto.AppointmentVerifiedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppointmentConsumer {

    @KafkaListener(
            topics = "appointment-created",
            groupId = "notification-group"
    )
    public void consume(
            AppointmentCreatedEvent appointmentCreatedEvent)
    {log.info("Consumer received appointment created event: {}",
                appointmentCreatedEvent);
    if (appointmentCreatedEvent.appointmentId() == 31) {
    throw new RuntimeException();}
    }

    @KafkaListener(
            topics = "appointment-verified",
            groupId = "notification-group"
    )
    public void consumeVerified(
            AppointmentVerifiedEvent appointmentVerifiedEvent) {
        log.info(
                "Appointment verified event received: {}",
                appointmentVerifiedEvent
        );
    }
    @KafkaListener(
            topics = "appointment-created-dlt",
            groupId = "notification-group")
    public void consumeDlt(
            AppointmentCreatedEvent appointmentCreatedEvent) {
        log.error( "DLQ EVENT RECEIVED: {}",
                appointmentCreatedEvent
        );
    }

}