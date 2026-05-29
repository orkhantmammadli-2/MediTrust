package com.ltc.notificationservice.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltc.sharedevents.dto.AppointmentCreatedEvent;
import com.ltc.sharedevents.webhook.HmacUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static java.security.KeyRep.Type.SECRET;

@Service
@RequiredArgsConstructor
public class WebhookService {

    private static final String SECRET = "medi-trust-webhook-secret";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public void sendDlqAlert(
            AppointmentCreatedEvent event
    ) {

        try {

            String payload =
                    objectMapper.writeValueAsString(event);

            String signature =
                    HmacUtil.generateHmac(
                            payload,
                            SECRET
                    );

            HttpHeaders headers =
                    new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_JSON
            );

            headers.set(
                    "X-Signature",
                    signature
            );

            HttpEntity<String> request =
                    new HttpEntity<>(
                            payload,
                            headers
                    );

            restTemplate.postForEntity(
                    "http://localhost:8082/api/v1/webhooks/dlq",
                    request,
                    Void.class
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Webhook send failed",
                    e
            );
        }
    }
}