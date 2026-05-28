package com.ltc.notificationservice.kafka;

import com.ltc.notificationservice.email.EmailService;
import com.ltc.notificationservice.email.EmailTemplateService;
import com.ltc.sharedevents.dto.AppointmentCreatedEvent;
import com.ltc.sharedevents.dto.AppointmentVerifiedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppointmentConsumer {
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;
    public AppointmentConsumer(EmailService emailService, EmailTemplateService emailTemplateService) {
        this.emailService = emailService;
        this.emailTemplateService = emailTemplateService;
    }
    @KafkaListener(
            topics = "appointment-created",
            groupId = "notification-group"
    )
    public void consume(
            AppointmentCreatedEvent appointmentCreatedEvent)
    {   String html = emailTemplateService.buildAppointmentCreated(appointmentCreatedEvent);
        emailService.sendHtmlEmail(
            "orkhantmammadli@outlook.com",
            "New Appointment",
            html);
        log.info("Consumer received appointment created event: {}",
                appointmentCreatedEvent);
    if (appointmentCreatedEvent.appointmentId() == 41) {
    throw new RuntimeException();}
    }

    @KafkaListener(
            topics = "appointment-verified",
            groupId = "notification-group"
    )
    public void consumeVerified(
            AppointmentVerifiedEvent appointmentVerifiedEvent) {
        String html = emailTemplateService.buildAppointmentVerified(appointmentVerifiedEvent);
        emailService.sendHtmlEmail("orkhantmammadli@outlook.com",
                "Verified Appointment",html);
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