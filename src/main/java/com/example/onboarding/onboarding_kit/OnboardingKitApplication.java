package com.example.onboarding.onboarding_kit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.example.onboarding"})
@SpringBootApplication
public class OnboardingKitApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnboardingKitApplication.class, args);
	}

}
