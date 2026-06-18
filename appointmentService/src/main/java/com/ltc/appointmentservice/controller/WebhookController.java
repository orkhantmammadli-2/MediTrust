package com.ltc.appointmentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltc.appointmentservice.configuration.HmacUtil;
import com.ltc.sharedevents.dto.AppointmentCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

    private static final Logger dlqLogger =
            LoggerFactory.getLogger("DLQ_LOGGER");

    private static final String SECRET =
            "medi-trust-webhook-secret";

    private final ObjectMapper objectMapper;

    @PostMapping("/dlq")
    public ResponseEntity<Void> receiveDlqAlert(

            @RequestHeader("X-Signature")
            String signature,

            @RequestBody String payload

    ) {

        try {

            String expectedSignature =
                    HmacUtil.generateHmac(
                            payload,
                            SECRET
                    );

            if (!expectedSignature.equals(signature)) {

                dlqLogger.error(
                        "INVALID WEBHOOK SIGNATURE"
                );

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .build();
            }

            AppointmentCreatedEvent event =
                    objectMapper.readValue(
                            payload,
                            AppointmentCreatedEvent.class
                    );

            dlqLogger.error(
                    "DLQ ALERT RECEIVED: {}",
                    event
            );

            return ResponseEntity.ok().build();

        } catch (Exception e) {

            dlqLogger.error(
                    "WEBHOOK PROCESSING FAILED",
                    e
            );

            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }
}