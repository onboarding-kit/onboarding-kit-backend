package com.example.onboardingkitbackend.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String getAllEmails() {
        return "Hello World!";
    }

}
