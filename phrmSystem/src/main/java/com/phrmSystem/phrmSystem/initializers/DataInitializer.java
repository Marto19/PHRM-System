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
    private final SickDayRepository sickDayRepository;
    private final MedicineRepository medicineRepository;
    private final DoctorAppointmentRepository doctorAppointmentRepository;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           DoctorSpecializationRepository specializationRepository,
                           EntityManager entityManager, DiagnosisRepository diagnosisRepository, SickDayRepository sickDayRepository, MedicineRepository medicineRepository, DoctorAppointmentRepository doctorAppointmentRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.specializationRepository = specializationRepository;
        this.entityManager = entityManager;
        this.diagnosisRepository = diagnosisRepository;
        this.sickDayRepository = sickDayRepository;
        this.medicineRepository = medicineRepository;
        this.doctorAppointmentRepository = doctorAppointmentRepository;
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

        User doctor2 = new User();
        doctor2.setFirstName("Ivan");
        doctor2.setLastName("Gradarov");
        doctor2.setUniqueId("DOC133");
        doctor2.setIsPersonalDoctor(true);
        doctor2.setRole(List.of(doctorRole));
        userRepository.save(doctor2);

        User doctor3 = new User();
        doctor3.setFirstName("Just");
        doctor3.setLastName("John");
        doctor3.setUniqueId("DOC234");
        doctor3.setIsPersonalDoctor(true);
        doctor3.setRole(List.of(doctorRole));
        userRepository.save(doctor3);

        User patient = new User();
        patient.setFirstName("Alice");
        patient.setLastName("Smith");
        patient.setUniqueId("PAT001");
        patient.setIsPersonalDoctor(false);
        patient.setRole(List.of(patientRole));
        userRepository.save(patient);

        // Add Diagnoses
        Diagnosis fluDiagnosis = new Diagnosis();
        fluDiagnosis.setDiagnosisName("Flu");
        fluDiagnosis.setDiagnosisDescription("Seasonal flu");
        diagnosisRepository.save(fluDiagnosis);

        Diagnosis coldDiagnosis = new Diagnosis();
        coldDiagnosis.setDiagnosisName("Cold");
        coldDiagnosis.setDiagnosisDescription("Common cold");
        diagnosisRepository.save(coldDiagnosis);

        // Add doctor specializations
        DoctorSpecialization specialization1 = new DoctorSpecialization();
        specialization1.setSpecialization("Cardiology");
        specializationRepository.save(specialization1);

        DoctorSpecialization specialization2 = new DoctorSpecialization();
        specialization2.setSpecialization("Neurology");
        specializationRepository.save(specialization2);

        DoctorSpecialization specialization3 = new DoctorSpecialization();
        specialization3.setSpecialization("Pediatrics");
        specializationRepository.save(specialization3);

        DoctorSpecialization specialization4 = new DoctorSpecialization();
        specialization4.setSpecialization("Dermatology");
        specializationRepository.save(specialization4);

        DoctorSpecialization specialization5 = new DoctorSpecialization();
        specialization5.setSpecialization("Orthopedics");
        specializationRepository.save(specialization5);

        SickDay sickDay1 = new SickDay();
        sickDay1.setStartDate(LocalDate.of(2025, 1, 1));
        sickDay1.setEndDate(LocalDate.of(2025, 1, 7));
        sickDay1.setNumberOfDays(7);
        sickDay1.setPatient(patient);
        sickDay1.setDoctor(doctor);
        sickDay1.setDiagnosis(Set.of(fluDiagnosis));
        sickDayRepository.save(sickDay1);

        SickDay sickDay2 = new SickDay();
        sickDay2.setStartDate(LocalDate.of(2025, 2, 10));
        sickDay2.setEndDate(LocalDate.of(2025, 2, 14));
        sickDay2.setNumberOfDays(5);
        sickDay2.setPatient(patient);
        sickDay2.setDoctor(doctor);
        sickDay2.setDiagnosis(Set.of(coldDiagnosis));
        sickDayRepository.save(sickDay2);

        SickDay sickDay3 = new SickDay();
        sickDay3.setStartDate(LocalDate.of(2025, 3, 15));
        sickDay3.setEndDate(LocalDate.of(2025, 3, 20));
        sickDay3.setNumberOfDays(6);
        sickDay3.setPatient(patient);
        sickDay3.setDoctor(doctor);
        sickDay3.setDiagnosis(Set.of(fluDiagnosis, coldDiagnosis));
        sickDayRepository.save(sickDay3);

        SickDay sickDay4 = new SickDay();
        sickDay4.setStartDate(LocalDate.of(2025, 4, 5));
        sickDay4.setEndDate(LocalDate.of(2025, 4, 9));
        sickDay4.setNumberOfDays(4);
        sickDay4.setPatient(patient);
        sickDay4.setDoctor(doctor);
        sickDay4.setDiagnosis(Set.of(fluDiagnosis));
        sickDayRepository.save(sickDay4);

        SickDay sickDay5 = new SickDay();
        sickDay5.setStartDate(LocalDate.of(2025, 5, 20));
        sickDay5.setEndDate(LocalDate.of(2025, 5, 25));
        sickDay5.setNumberOfDays(6);
        sickDay5.setPatient(patient);
        sickDay5.setDoctor(doctor);
        sickDay5.setDiagnosis(Set.of(coldDiagnosis));
        sickDayRepository.save(sickDay5);

        Medicine paracetamol = new Medicine();
        paracetamol.setMedicineName("Paracetamol");
        paracetamol.setMedicineDescription("Used for pain relief and fever reduction.");
        paracetamol.setDiagnosis(fluDiagnosis);
        medicineRepository.save(paracetamol);

        Medicine ibuprofen = new Medicine();
        ibuprofen.setMedicineName("Ibuprofen");
        ibuprofen.setMedicineDescription("Used for pain relief and reducing inflammation.");
        ibuprofen.setDiagnosis(coldDiagnosis);
        medicineRepository.save(ibuprofen);

        // Add Doctor Appointments
        DoctorAppointment appointment1 = new DoctorAppointment();
        appointment1.setDate(LocalDateTime.of(2025, 1, 15, 10, 30));
        appointment1.setPatient(patient);
        appointment1.setDoctor(doctor);
        appointment1.setDiagnosis(Set.of(fluDiagnosis));
        doctorAppointmentRepository.save(appointment1);

        DoctorAppointment appointment2 = new DoctorAppointment();
        appointment2.setDate(LocalDateTime.of(2025, 2, 20, 14, 0));
        appointment2.setPatient(patient);
        appointment2.setDoctor(doctor);
        appointment2.setDiagnosis(Set.of(coldDiagnosis));
        doctorAppointmentRepository.save(appointment2);
    }

}
