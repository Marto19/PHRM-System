package com.phrmSystem.phrmSystem.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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

@AllArgsConstructor
public class SecurityConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        String issuerUri = "http://localhost:8080/realms/pharmacy-application-realm";
        return JwtDecoders.fromIssuerLocation(issuerUri);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakAuthorityConverter());
        return jwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests
                        (
                                authz -> authz
//                                        .requestMatchers("/recipes/**").hasAuthority("doctor")
//                                        .requestMatchers("/api/recipes/**").hasAuthority("doctor")
//                                        .requestMatchers("/medicines/**").hasAuthority("seller")
//                                        .requestMatchers("/api/medicines/**").hasAuthority("seller")
//                                        .requestMatchers(HttpMethod.GET, "/medicines").hasAuthority("customer")
//                                        .anyRequest().authenticated()
                                        .anyRequest().permitAll()
                        )
                .csrf(csrf -> csrf.disable())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtCustomizer -> jwtCustomizer
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .oauth2Login(Customizer.withDefaults());
        return http.build();
    }

}
