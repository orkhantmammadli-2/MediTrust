package com.ltc.patientservice.service;


import com.ltc.sharedevents.dto.AppointmentCreatedEvent;
import com.ltc.sharedevents.dto.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserRegisteredEvent(
            String email,
            String name
    ) {
        kafkaTemplate.send(
                "user-registration-topic",
                new UserRegisteredEvent(email, name)
        );
    }
}
