package com.phrmSystem.phrmSystem.config;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true
)

@RequiredArgsConstructor
public class SecurityConfig {


    private final  JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests
                        (
                                authz -> authz
                                        .requestMatchers("/api/users/**").hasAnyRole("client_admin") // Admin manages users
                                        .requestMatchers("/api/roles/**").hasRole("client_admin") // Admin manages roles
                                        .requestMatchers("/api/doctors/**").hasAnyRole("client_admin", "client_doctor") // Doctors and admins manage doctor data
                                        .requestMatchers("/api/patients/**").hasAnyRole("client_admin", "client_doctor", "client_patient") // Patients, doctors, and admins access patient data
                                        .requestMatchers("/api/appointments/**").hasAnyRole("client_admin", "client_doctor") // Appointments managed by doctors and admins
                                        .requestMatchers("/api/medicines/**").hasAnyRole("client_admin", "client_doctor") // Medicines assigned by doctors, managed by admins
                                        .requestMatchers("/api/diagnoses/**").hasAnyRole("client_admin", "client_doctor") // Diagnoses managed by doctors and admins
                                        .requestMatchers("/api/sick-days/**").hasAnyRole("client_admin", "client_doctor") // Sick days managed by doctors and admins
                                        .requestMatchers("/api/illness-histories/**").hasAnyRole("client_admin", "client_doctor") // Illness histories accessed by doctors and admins
                                        .requestMatchers("/api/demo/**").permitAll() // Public endpoints for demonstration purposes
                                        .anyRequest().authenticated() // All other requests require authentication
//                                        .anyRequest().permitAll()
                        )
                .csrf(csrf -> csrf.disable())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtCustomizer -> jwtCustomizer
                                .jwtAuthenticationConverter(jwtAuthConverter)))
                .oauth2Login(Customizer.withDefaults())
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }


}
