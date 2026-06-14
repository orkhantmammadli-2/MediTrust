package com.ltc.appointmentservice.repository;

import com.ltc.appointmentservice.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>,
        JpaSpecificationExecutor<Appointment> {
    List<Appointment> findByPatientId(Long patientId);
    Page<Appointment> findByAdmissionVerifiedTrue(Pageable pageable);
    @Query("""
    SELECT a.complaintType,
       COUNT(a)
    FROM Appointment a
    GROUP BY a.complaintType
    ORDER BY COUNT(a) DESC
    """)
    List<Object[]> findTopComplaintType();
    @Query("""
    SELECT a.appointmentPlace,
       COUNT(a)
    FROM Appointment a
    GROUP BY a.appointmentPlace
    ORDER BY COUNT(a) DESC
    """)
    List<Object[]> findTopHospital();
}
