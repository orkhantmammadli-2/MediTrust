package com.ltc.appointmentservice.repository;

import com.ltc.appointmentservice.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    @Query("""
        SELECT a FROM Appointment a
        WHERE LOWER(a.doctorName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(a.complaintType) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(a.appointmentPlace) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(a.likedAspect1) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(a.likedAspect2) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """)
    List<Appointment> searchAppointments(@Param("keyword") String keyword);

}
