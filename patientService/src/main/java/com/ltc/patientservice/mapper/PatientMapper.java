package com.ltc.patientservice.mapper;

import com.ltc.patientservice.dto.PatientRequest;
import com.ltc.patientservice.dto.PatientResponse;
import com.ltc.patientservice.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    Patient toEntity(PatientRequest request);
    PatientResponse toResponse(Patient patient);
    void updateEntity(PatientRequest request, @MappingTarget Patient entity);

}
