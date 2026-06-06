package com.ltc.appointmentservice.controller;

import com.ltc.appointmentservice.dto.AppointmentFilter;
import com.ltc.appointmentservice.dto.AppointmentSpecifications;
import com.ltc.appointmentservice.entity.Appointment;
import com.ltc.appointmentservice.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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
            @Argument AppointmentFilter filter
    ) {

        Specification<Appointment> spec =
                Specification.allOf(

                        AppointmentSpecifications
                                .hasDoctorName(
                                        filter.doctorName()
                                ),

                        AppointmentSpecifications
                                .hasComplaintType(
                                        filter.complaintType()
                                ),

                        AppointmentSpecifications
                                .hasAppointmentPlace(
                                        filter.appointmentPlace()
                                ),

                        AppointmentSpecifications
                                .hasLikedAspect1(
                                        filter.likedAspect1()
                                ),
                        AppointmentSpecifications
                                .hasLikedAspect2(
                                        filter.likedAspect2()
                                ),
                        AppointmentSpecifications
                                .hasRating(
                                        filter.rating()
                                ),
                        AppointmentSpecifications
                                .hasFeedback(
                                        filter.feedback()
                                ),
                        AppointmentSpecifications
                                .hasDocument(
                                        filter.hasDocument()
                                )

                );

        return repository.findAll(spec);
    }

}
