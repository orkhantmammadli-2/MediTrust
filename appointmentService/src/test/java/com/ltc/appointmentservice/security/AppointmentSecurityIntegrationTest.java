package com.ltc.appointmentservice.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AppointmentSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn401WithoutToken() throws Exception {

        mockMvc.perform(get("/api/v1/appointments/1"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
