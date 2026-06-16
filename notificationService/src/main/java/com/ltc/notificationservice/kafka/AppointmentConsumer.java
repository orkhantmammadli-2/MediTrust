package com.ltc.notificationservice.kafka;

import com.ltc.notificationservice.email.EmailService;
import com.ltc.notificationservice.email.EmailTemplateService;
import com.ltc.notificationservice.webhook.TelegramService;
import com.ltc.notificationservice.webhook.WebhookService;
import com.ltc.sharedevents.dto.AppointmentCreatedEvent;
import com.ltc.sharedevents.dto.AppointmentVerifiedEvent;
import com.ltc.sharedevents.dto.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppointmentConsumer {
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;
    private final WebhookService webhookService;
    private final TelegramService telegramService;
    public AppointmentConsumer(EmailService emailService, EmailTemplateService emailTemplateService, WebhookService webhookService, TelegramService telegramService) {
        this.emailService = emailService;
        this.emailTemplateService = emailTemplateService;
        this.webhookService = webhookService;
        this.telegramService = telegramService;
    }


    @KafkaListener(
            topics = "appointment-created",
            groupId = "notification-group"
    )
    public void consume(
            AppointmentCreatedEvent appointmentCreatedEvent)
    {   if (appointmentCreatedEvent.appointmentId() == 20) {throw new RuntimeException();}
        String html = emailTemplateService.buildAppointmentCreated(appointmentCreatedEvent);
        emailService.sendHtmlEmail(
            "orkhantmammadli@outlook.com",
            "New Appointment",
            html);
        log.info("Consumer received appointment created event: {}",
                appointmentCreatedEvent);
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
                "Consumer received appointment verified event: {}",
                appointmentVerifiedEvent
        );
    }
    private static final Logger dlqLogger =
            LoggerFactory.getLogger("DLQ_LOGGER");
    @KafkaListener(
            topics = "appointment-created-dlt",
            groupId = "notification-group")
    public void consumeDlt(
            AppointmentCreatedEvent appointmentCreatedEvent) {
        dlqLogger.error( "DLQ EVENT RECEIVED: {}",
                appointmentCreatedEvent);
        telegramService.sendDlqAlert(appointmentCreatedEvent);
        webhookService.sendDlqAlert(appointmentCreatedEvent);
    }
    @KafkaListener(
            topics = "user-registration-topic",
            groupId = "notification-group"
    )
    public void handleUserRegistration(
            UserRegisteredEvent event
    ) {

        log.info(
                "Consumer received User registration event : {}",
                event
        );

        String html = """
        <html>
        <body>
            <h2>Welcome to MediTrust AI</h2>
            <p>Dear %s,</p>
            <p>Your account has been created successfully.</p>
        </body>
        </html>
        """
                .formatted(event.name());

        emailService.sendHtmlEmail(
                "orkhantmammadli@outlook.com",
                "Welcome to MediTrust AI",
                html
        );

        log.info(
                "Welcome email sent to {}",
                event.email()
        );
    }
    }

