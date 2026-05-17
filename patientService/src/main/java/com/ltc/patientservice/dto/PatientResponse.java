package com.ltc.patientservice.dto;


import com.ltc.patientservice.entity.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String email;
    private String mobileNumber;
}
