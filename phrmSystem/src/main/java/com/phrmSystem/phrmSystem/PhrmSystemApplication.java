package com.phrmSystem.phrmSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableMethodSecurity
public class PhrmSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhrmSystemApplication.class, args);
	}

}


//TODO: FIX DOCTOR SPECIALIZATION JUNCTION TABLE WEB VIEW- RECORDS NOT RECORDING IN THERE