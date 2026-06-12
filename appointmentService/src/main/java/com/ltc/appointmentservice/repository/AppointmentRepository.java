package com.ltc.appointmentservice.repository;

import com.ltc.appointmentservice.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>,
        JpaSpecificationExecutor<Appointment> {
    List<Appointment> findByPatientId(Long patientId);
    Page<Appointment> findByAdmissionVerifiedTrue(Pageable pageable);
}
