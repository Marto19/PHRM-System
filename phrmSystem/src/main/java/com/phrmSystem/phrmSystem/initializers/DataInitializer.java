package com.phrmSystem.phrmSystem.initializers;

import com.phrmSystem.phrmSystem.data.entity.Role;
import com.phrmSystem.phrmSystem.data.repo.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (!roleRepository.existsByRoleName("DOCTOR")) {
            Role doctorRole = new Role();
            doctorRole.setRoleName("DOCTOR");
            doctorRole.setDescription("Doctor role with appropriate permissions.");
            roleRepository.save(doctorRole);
        }

        if (!roleRepository.existsByRoleName("PATIENT")) {
            Role patientRole = new Role();
            patientRole.setRoleName("PATIENT");
            patientRole.setDescription("Patient role.");
            roleRepository.save(patientRole);
        }
        // Add other roles as needed
    }
}
