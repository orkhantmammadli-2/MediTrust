package com.ltc.patientservice.service;

import com.ltc.patientservice.dto.PatientResponse;
import com.ltc.patientservice.entity.Patient;
import com.ltc.patientservice.exception.PatientNotFound;
import com.ltc.patientservice.mapper.PatientMapper;
import com.ltc.patientservice.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    void shouldReturnPatientById() {

        Patient patient = new Patient();
        patient.setId(1L);
        PatientResponse response = new PatientResponse();
        response.setId(1L);
        response.setFirstName("Orxan");
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientMapper.toResponse(patient)).thenReturn(response);
        PatientResponse result = patientService.getById(1L);
        assertEquals(1L, result.getId());
        assertEquals("Orxan", result.getFirstName());
        verify(patientRepository).findById(1L);
        verify(patientMapper).toResponse(patient);
    }

    @Test
    void shouldThrowExceptionWhenPatientNotFound() {
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(PatientNotFound.class, () -> patientService.getById(999L));
        verify(patientRepository).findById(999L);
    }
}
