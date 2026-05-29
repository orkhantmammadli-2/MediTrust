package com.ltc.appointmentservice.controller;

import com.ltc.appointmentservice.entity.Appointment;
import com.ltc.appointmentservice.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Controller
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AppointmentGraphQLController {

    private final AppointmentRepository repository;

    @QueryMapping
    public List<Appointment> getAllAppointments() {
        return repository.findAll();
    }

    @QueryMapping
    public Appointment getAppointmentById(
            @Argument Long id
    ) {
        return repository.findById(id)
                .orElseThrow();
    }
    @QueryMapping
    public List<Appointment> searchAppointments(
            @Argument String keyword
    ) {
        return repository.searchAppointments(keyword);
    }
}
