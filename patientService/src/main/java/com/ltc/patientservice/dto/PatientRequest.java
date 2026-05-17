package com.ltc.patientservice.dto;

import com.ltc.patientservice.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PatientRequest {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    @Email(message = "Email formatına uyğun olsun.")
    private String email;
    @Pattern(regexp = "^\\+[0-9]{12}$",
            message = "Mobil nömrə '+' ilə başlasın və bundan sonra 12 rəqəm olsun.")
    private String mobileNumber;
}