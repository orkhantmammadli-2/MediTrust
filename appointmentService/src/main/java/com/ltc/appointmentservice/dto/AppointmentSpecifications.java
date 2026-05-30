package com.ltc.appointmentservice.dto;

import com.ltc.appointmentservice.entity.Appointment;
import org.springframework.data.jpa.domain.Specification;

public class AppointmentSpecifications {
    public static Specification<Appointment>
    hasDoctorName(String doctorName) {
        return (root, query, cb) ->
                doctorName == null ||
                        doctorName.isBlank()
                        ? null
                        :
                        cb.like(
                                cb.lower(
                                        root.get("doctorName")
                                ),
                                "%" +
                                        doctorName.toLowerCase()
                                + "%"
                        );
    }
    public static Specification<Appointment>
    hasComplaintType(String complaintType) {

        return (root, query, cb) ->

                complaintType == null ||
                        complaintType.isBlank()

                        ? null

                        :

                        cb.like(
                                cb.lower(
                                        root.get("complaintType")
                                ),
                                "%" +
                                        complaintType.toLowerCase()
                                + "%"
                        );
    }
    public static Specification<Appointment>
    hasAppointmentPlace(String appointmentPlace) {

        return (root, query, cb) ->

                appointmentPlace == null ||
                        appointmentPlace.isBlank()

                        ? null

                        :

                        cb.like(
                                cb.lower(
                                        root.get("appointmentPlace")
                                ),
                                "%" +
                                        appointmentPlace.toLowerCase()
                                + "%"
                        );
    }
    public static Specification<Appointment>
    isVerified(Boolean verified) {

        return (root, query, cb) ->

                verified == null

                        ? null

                        :

                        cb.equal(
                                root.get("admissionVerified"),
                                verified
                        );
    }
    public static Specification<Appointment>
    hasLikedAspect1(String likedAspect1) {

        return (root, query, cb) ->

                likedAspect1 == null ||
                        likedAspect1.isBlank()

                        ? null

                        :

                        cb.like(
                                cb.lower(
                                        root.get("likedAspect1")
                                ),
                                "%" +
                                        likedAspect1.toLowerCase()
                                + "%"
                        );
    }
    public static Specification<Appointment>
    hasLikedAspect2(String likedAspect2) {

        return (root, query, cb) ->

                likedAspect2 == null ||
                        likedAspect2.isBlank()

                        ? null

                        :

                        cb.like(
                                cb.lower(
                                        root.get("likedAspect1")
                                ),
                                "%" +
                                        likedAspect2.toLowerCase()
                                + "%"
                        );
    }
    public static Specification<Appointment>
    hasRating(String rating) {

        return (root, query, cb) ->

                rating == null ||
                        rating.isBlank()

                        ? null

                        :

                        cb.like(
                                cb.lower(
                                        root.get("likedAspect1")
                                ),
                                "%" +
                                        rating.toLowerCase()
                                + "%"
                        );
    }
    public static Specification<Appointment>
    hasFeedback(String fedback) {

        return (root, query, cb) ->

                fedback == null ||
                        fedback.isBlank()

                        ? null

                        :

                        cb.like(
                                cb.lower(
                                        root.get("likedAspect1")
                                ),
                                "%" +
                                        fedback.toLowerCase()
                                + "%"
                        );
    }
}
