package com.ltc.appointmentservice.repository;

import com.ltc.appointmentservice.entity.Appointment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldFindTopComplaintType() {

        Appointment a1 = new Appointment();
        a1.setPatientId(1L);
        a1.setAppointmentDate(LocalDateTime.now());
        a1.setAppointmentPlace("LOR Hospital");
        a1.setDoctorName("Dr. House");
        a1.setComplaintType("Burun");
        a1.setFeedback("Excellent doctor");
        a1.setLikedAspect1("Politeness");
        a1.setLikedAspect2("Experience");

        Appointment a2 = new Appointment();
        a2.setPatientId(2L);
        a2.setAppointmentDate(LocalDateTime.now());
        a2.setAppointmentPlace("Central Hospital");
        a2.setDoctorName("Dr. House");
        a2.setComplaintType("Burun");
        a2.setFeedback("Very good");
        a2.setLikedAspect1("Politeness");
        a2.setLikedAspect2("Experience");

        Appointment a3 = new Appointment();
        a3.setPatientId(3L);
        a3.setAppointmentDate(LocalDateTime.now());
        a3.setAppointmentPlace("Central Hospital");
        a3.setDoctorName("Dr. Strange");
        a3.setComplaintType("Bel");
        a3.setFeedback("Good service");
        a3.setLikedAspect1("Experience");
        a3.setLikedAspect2("Cleanliness");

        entityManager.persist(a1);
        entityManager.persist(a2);
        entityManager.persist(a3);

        entityManager.flush();

        List<Object[]> result = appointmentRepository.findTopComplaintType();
        assertFalse(result.isEmpty());
        assertEquals("Burun", result.get(0)[0]);
        assertEquals(2L, result.get(0)[1]);

    }
}
