package com.ltc.appointmentservice.feign;


import com.ltc.appointmentservice.configuration.FeignConfig;
import com.ltc.appointmentservice.dto.PatientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "patient-service",
        url = "http://localhost:8081",
        configuration = FeignConfig.class
)
public interface PatientClient {

    @GetMapping("/api/v1/patients/{id}")
    PatientResponse getPatientById(
            @PathVariable("id") Long id
    );
}
