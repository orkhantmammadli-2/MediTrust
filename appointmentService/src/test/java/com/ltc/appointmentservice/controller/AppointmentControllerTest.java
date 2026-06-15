package com.ltc.appointmentservice.controller;

import com.ltc.appointmentservice.configuration.JwtAuthenticationFilter;
import com.ltc.appointmentservice.dto.AppointmentResponse;
import com.ltc.appointmentservice.entity.Rating;
import com.ltc.appointmentservice.service.AppointmentServiceImpl;
import com.ltc.appointmentservice.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppointmentServiceImpl appointmentServiceImpl;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Test
    void shouldReturnAppointmentById() throws Exception {

        AppointmentResponse response = new AppointmentResponse();

        response.setId(1L);
        response.setPatientId(1L);
        response.setAppointmentDate(
                LocalDateTime.of(2026, 6, 13, 10, 0)
        );
        response.setAppointmentPlace("Central Hospital");
        response.setDoctorName("Dr. House");
        response.setComplaintType("Burun");
        response.setRating(Rating.THE_BEST);
        response.setFeedback("Doctor was very professional and helpful.");
        response.setLikedAspect1("Politeness");
        response.setLikedAspect2("Experience");
        response.setAdmissionDocumentPath(
                "https://medi-trust-ai-files-697374140390-eu-north-1-an.s3.eu-north-1.amazonaws.com/e6b5d1b8-3efc-4b13-9d2b-c3b00d14c21b-grey-geometrical-shapes-background.jpg"
        );
        response.setAdmissionVerified(true);

        when(appointmentServiceImpl.getById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.doctorName").value("Dr. House"))
                .andExpect(jsonPath("$.complaintType").value("Burun"))
                .andExpect(jsonPath("$.appointmentPlace").value("Central Hospital"))
                .andExpect(jsonPath("$.admissionVerified").value(true))
                .andDo(print());

        verify(appointmentServiceImpl).getById(1L);
    }
}
