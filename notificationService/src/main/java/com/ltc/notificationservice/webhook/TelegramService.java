package com.ltc.notificationservice.webhook;

import com.ltc.sharedevents.dto.AppointmentCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TelegramService {

    private final RestTemplate restTemplate;

    private static final String TOKEN =
            "8900928722:AAF64KY0nqWwtkAl_ll9C7VdI-Ah9l4neUc";

    private static final String CHAT_ID =
            "6303911380";

    public void sendDlqAlert(
            AppointmentCreatedEvent event
    ) {

        String message = """
                🚨 DLQ ALERT

                Appointment ID: %s
                Patient ID: %s

                Doctor: %s
                Complaint: %s

                Event moved to DLQ.
                """.formatted(
                event.appointmentId(),
                event.patientId(),
                event.doctorName(),
                event.complaintType()
        );

        String url =
                "https://api.telegram.org/bot"
                        + TOKEN
                        + "/sendMessage";

        Map<String, String> body =
                Map.of(
                        "chat_id",
                        CHAT_ID,
                        "text",
                        message
                );

        restTemplate.postForEntity(
                url,
                body,
                String.class
        );
    }
}