package com.ltc.appointmentservice.controller;

import com.ltc.appointmentservice.configuration.JwtAuthenticationFilter;
import com.ltc.appointmentservice.dto.MonthlyInsightResponse;
import com.ltc.appointmentservice.service.AppointmentServiceImpl;
import com.ltc.appointmentservice.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(AppointmentController.class)
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
    void shouldReturnMonthlyInsights() throws Exception {

        MonthlyInsightResponse response =
                new MonthlyInsightResponse(
                        "Burun",
                        3L,
                        "LOR Hospital",
                        4L,
                        "AI Summary"
                );

        when(appointmentServiceImpl.getMonthlyInsights())
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/v1/appointments/insights/monthly")
                )
                .andDo(print());
    }
}
