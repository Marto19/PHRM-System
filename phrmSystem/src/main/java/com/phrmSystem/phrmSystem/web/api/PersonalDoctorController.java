package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.repo.DoctorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/personal-doctors")
public class PersonalDoctorController {

    private final DoctorRepository doctorRepository;

    public PersonalDoctorController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }


}
