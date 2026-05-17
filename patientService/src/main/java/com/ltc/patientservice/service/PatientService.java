package com.ltc.patientservice.service;


import com.ltc.patientservice.dto.PatientRequest;
import com.ltc.patientservice.dto.PatientResponse;

import java.util.List;

public interface PatientService {
    PatientResponse create(PatientRequest request);
    PatientResponse getById(Long id);
    List<PatientResponse> getAll();
    PatientResponse update(Long id, PatientRequest request);
    void delete(Long id);
}
