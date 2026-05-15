package com.ltc.meditrust.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
public class FallbackController {

    @RequestMapping("/fallback/patient")
    public Mono<ResponseEntity<String>> patientFallback() {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Patient Service is currently unavailable. Please try again later."));
    }
}
