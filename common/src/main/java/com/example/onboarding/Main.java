package com.example.onboarding;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello common world!");
    }
}