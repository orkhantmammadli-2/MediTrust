package com.ltc.patientservice.repository;

import com.ltc.patientservice.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByMobileNumber(String mobileNumber);

}
