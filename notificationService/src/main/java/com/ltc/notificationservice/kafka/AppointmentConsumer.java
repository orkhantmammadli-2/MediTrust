package com.ltc.notificationservice.kafka;

import com.ltc.notificationservice.email.EmailService;
import com.ltc.notificationservice.email.EmailTemplateService;
import com.ltc.notificationservice.webhook.TelegramService;
import com.ltc.notificationservice.webhook.WebhookService;
import com.ltc.sharedevents.dto.AppointmentCreatedEvent;
import com.ltc.sharedevents.dto.AppointmentVerifiedEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
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
            topics = "appointment-created-v2",
            groupId = "notification-group"
    )
    public void consume(
            AppointmentCreatedEvent appointmentCreatedEvent) {
        if (appointmentCreatedEvent.appointmentId() == 19) {
            throw new RuntimeException();
        }
        String html = emailTemplateService.buildAppointmentCreated(appointmentCreatedEvent);
        emailService.sendHtmlEmail(
                "orkhantmammadli@outlook.com",
                "New Appointment",
                html);
        log.info("Consumer received appointment created event: {}",
                appointmentCreatedEvent);
    }

    @KafkaListener(
            topics = "appointment-verified-v2",
            groupId = "notification-group"
    )
    public void consumeVerified(
            AppointmentVerifiedEvent appointmentVerifiedEvent) {
        String html = emailTemplateService.buildAppointmentVerified(appointmentVerifiedEvent);
        emailService.sendHtmlEmail("orkhantmammadli@outlook.com",
                "Verified Appointment", html);
        log.info(
                "Consumer received appointment verified event: {}",
                appointmentVerifiedEvent
        );
    }

    private static final Logger dlqLogger =
            LoggerFactory.getLogger("DLQ_LOGGER");

    @KafkaListener(
            topics = "appointment-created-v2-dlt",
            groupId = "notification-group")
    public void consumeDlt(
            AppointmentCreatedEvent event) {

        dlqLogger.error(
                "DLQ EVENT RECEIVED: {}",
                event);

        try {
            telegramService.sendDlqAlert(event);
            System.out.println("TELEGRAM OK");
        } catch (Exception e) {
            log.error("Telegram error", e);
        }

        try {
            webhookService.sendDlqAlert(event);
            System.out.println("WEBHOOK OK");
        } catch (Exception e) {
            log.error("Webhook error", e);
        }
    }
    }


