package com.ltc.patientservice.service;

import com.ltc.patientservice.dto.PatientRequest;
import com.ltc.patientservice.dto.PatientResponse;
import com.ltc.patientservice.entity.Patient;
import com.ltc.patientservice.exception.PatientAlreadyExists;
import com.ltc.patientservice.exception.PatientNotFound;
import com.ltc.patientservice.mapper.PatientMapper;
import com.ltc.patientservice.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j


public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    public PatientServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    @Override
    public PatientResponse create(PatientRequest request) {
        if (patientRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new PatientAlreadyExists("Patient already exists!");}
        Patient patient = patientMapper.toEntity(request);
        Patient saved = patientRepository.save(patient);
        log.info("Patient created with id={}", saved.getId());
        return patientMapper.toResponse(saved);
    }

    @Override
    public PatientResponse getById(Long id) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new PatientNotFound("Patient not found"));
        log.info("Patient found with id={}", patient.getId());
        return patientMapper.toResponse(patient);
    }


    @Override
    public List<PatientResponse> getAll() {
        log.info("All Patient found");
        return patientRepository
                .findAll()
                .stream()
                .map(patientMapper::toResponse)
                .toList();}

    @Override
    public PatientResponse update(Long id, PatientRequest request) {
        Patient patient = patientRepository
                .findById(id).orElseThrow(() -> new PatientNotFound("Patient not found"));
        if (!patient.getMobileNumber().equals(request.getMobileNumber())
                && patientRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new PatientAlreadyExists("Patient already exists!");}
            patientMapper.updateEntity(request, patient);
            patientRepository.save(patient);
            log.info("Patient updated with id={}", patient.getId());
            return patientMapper.toResponse(patient);}

    @Override
    public void delete(Long id) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new PatientNotFound("Patient not found"));
        patientRepository.delete(patient);
        log.info("Patient deleted with id={}", id);
    }
}
