package com.phrmSystem.phrmSystem.web.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.processing.Generated;
import javax.print.DocFlavor;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping
    @PreAuthorize("hasRole('client_patient')")
    public String hello(){
        return "Hello from spring boot and keycloak";
    }

    @GetMapping("/hello2")
    @PreAuthorize("hasRole('client_admin')")
    public String hello2(){
        return "Hello from spring boot and keycloak - ADMIN";
    }
}
