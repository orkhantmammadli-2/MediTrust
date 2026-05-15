package com.ltc.patientservice.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patient")
public class TestController {
    @GetMapping
    public String getPatient(){
        return "testPatient";
    }
}
