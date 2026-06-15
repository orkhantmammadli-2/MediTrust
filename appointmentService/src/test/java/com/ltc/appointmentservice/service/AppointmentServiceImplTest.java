package com.ltc.appointmentservice.service;

import com.ltc.appointmentservice.dto.MonthlyInsightResponse;
import com.ltc.appointmentservice.feign.AiClient;
import com.ltc.appointmentservice.feign.PatientClient;
import com.ltc.appointmentservice.mapper.AppointmentMapper;
import com.ltc.appointmentservice.repository.AppointmentRepository;
import com.ltc.sharedevents.dto.InsightRequest;
import com.ltc.sharedevents.dto.InsightResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private AppointmentMapper appointmentMapper;
    @Mock
    private PatientClient patientClient;
    @Mock
    private AiClient aiClient;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private SimpMessagingTemplate messagingTemplate;
    @Mock
    private KafkaProducerService kafkaProducerService;
    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    void shouldReturnMonthlyInsights() {

        Object[] complaint = new Object[]{"Burun", 3L};
        Object[] hospital = new Object[]{"LOR Hospital", 4L};

        List<Object[]> complaints = new ArrayList<>();
        complaints.add(complaint);
        List<Object[]> hospitals = new ArrayList<>();
        hospitals.add(hospital);

        when(appointmentRepository.findTopComplaintType()).thenReturn(complaints);
        when(appointmentRepository.findTopHospital()).thenReturn(hospitals);
        when(aiClient.generateInsight(any(InsightRequest.class)))
                .thenReturn(new InsightResponse("AI summary"));
        MonthlyInsightResponse result = appointmentService.getMonthlyInsights();

        assertEquals("Burun", result.topComplaintType());
        assertEquals(3L, result.topComplaintCount());
        assertEquals("LOR Hospital", result.topHospital());
        assertEquals(4L, result.topHospitalVisitCount());
        assertEquals("AI summary", result.aiSummary());

        verify(appointmentRepository).findTopComplaintType();
        verify(appointmentRepository).findTopHospital();
        verify(aiClient).generateInsight(any(InsightRequest.class));

    }
}
