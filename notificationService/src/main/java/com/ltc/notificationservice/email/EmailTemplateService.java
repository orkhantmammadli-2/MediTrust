package com.ltc.notificationservice.email;

import com.ltc.sharedevents.dto.AppointmentCreatedEvent;
import com.ltc.sharedevents.dto.AppointmentVerifiedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final SpringTemplateEngine templateEngine;

    public String buildAppointmentCreated(AppointmentCreatedEvent event) {
        Context context = new Context();
        context.setVariable("id", event.appointmentId());
        context.setVariable("doctorName", event.doctorName());
        context.setVariable("place", event.appointmentPlace());
        context.setVariable("complaint", event.complaintType());
        return templateEngine.process("appointment-created", context);
    }

    public String buildAppointmentVerified(AppointmentVerifiedEvent event) {
        Context context = new Context();
        context.setVariable("id", event.appointmentId());
        context.setVariable("doctorName", event.doctorName());
        context.setVariable("place", event.appointmentPlace());
        context.setVariable("complaint", event.complaintType());
        return templateEngine.process("appointment-verified", context);
    }


}