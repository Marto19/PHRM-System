package com.phrmSystem.phrmSystem.initializers;

import com.phrmSystem.phrmSystem.data.entity.*;
import com.phrmSystem.phrmSystem.data.repo.*;
import jakarta.persistence.EntityManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DoctorSpecializationRepository specializationRepository;
    private final EntityManager entityManager;
    private final DiagnosisRepository diagnosisRepository;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           DoctorSpecializationRepository specializationRepository,
                           EntityManager entityManager, DiagnosisRepository diagnosisRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.specializationRepository = specializationRepository;
        this.entityManager = entityManager;
        this.diagnosisRepository = diagnosisRepository;
    }

    @Override
    public void run(String... args) {
        // Add roles
        Role doctorRole = roleRepository.findByRoleName("DOCTOR")
                .orElseGet(() -> roleRepository.save(new Role("DOCTOR", "Doctor role with appropriate permissions.", new LinkedList<>())));
        Role patientRole = roleRepository.findByRoleName("PATIENT")
                .orElseGet(() -> roleRepository.save(new Role("PATIENT", "Patient role.", new LinkedList<>())));

        // Add users
        User doctor = new User();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setUniqueId("DOC123");
        doctor.setIsPersonalDoctor(true);
        doctor.setRole(List.of(doctorRole));
        userRepository.save(doctor);

        // Add Diagnoses
        Diagnosis fluDiagnosis = new Diagnosis();
        fluDiagnosis.setDiagnosisName("Flu");
        fluDiagnosis.setDiagnosisDescription("Seasonal flu");
        diagnosisRepository.save(fluDiagnosis);

        Diagnosis coldDiagnosis = new Diagnosis();
        coldDiagnosis.setDiagnosisName("Cold");
        coldDiagnosis.setDiagnosisDescription("Common cold");
        diagnosisRepository.save(coldDiagnosis);
    }

//    private void associateSpecializationWithDoctor(User doctor, DoctorSpecialization specialization) {
//        String sql = "INSERT INTO doctor_specializations (doctor_id, specialization_id) VALUES (:doctorId, :specializationId)";
//        entityManager.createNativeQuery(sql)
//                .setParameter("specialization_Id", specialization.getId())
//                .setParameter("doctor_Id", doctor.getId())
//                .executeUpdate();
//    }

}
