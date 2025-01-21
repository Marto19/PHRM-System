package com.phrmSystem.phrmSystem.repo;

import com.phrmSystem.phrmSystem.data.entity.*;
import com.phrmSystem.phrmSystem.data.repo.DoctorRepository;
import com.phrmSystem.phrmSystem.data.repo.DoctorSpecializationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

@DataJpaTest
public class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorSpecializationRepository doctorSpecializationRepository;

    private User testDoctor;
    private DoctorSpecialization specialization1;
    private DoctorSpecialization specialization2;

    @BeforeEach
    public void setUp() {
        // Create specializations
        specialization1 = new DoctorSpecialization();
        specialization1.setSpecialization("Cardiologist");
        doctorSpecializationRepository.save(specialization1);

        specialization2 = new DoctorSpecialization();
        specialization2.setSpecialization("Neurologist");
        doctorSpecializationRepository.save(specialization2);

        // Create doctor
        testDoctor = new User();
        testDoctor.setFirstName("John");
        testDoctor.setLastName("Doe");
        testDoctor.setUniqueId("DOCTOR1");
        testDoctor.setIsPersonalDoctor(true);
        testDoctor.setSpecializations(Set.of(specialization1, specialization2));

        doctorRepository.save(testDoctor);
    }

    @Test
    public void testFindAllDoctors_ShouldReturnAllDoctors() {
        var doctors = doctorRepository.findAllDoctors();
        Assertions.assertNotNull(doctors);
        Assertions.assertFalse(doctors.isEmpty());
        Assertions.assertEquals(1, doctors.size());
        Assertions.assertEquals(testDoctor.getUniqueId(), doctors.get(0).getUniqueId());
    }

    @Test
    public void testFindAllPersonalDoctors_ShouldReturnPersonalDoctors() {
        var personalDoctors = doctorRepository.findAllPersonalDoctors();
        Assertions.assertNotNull(personalDoctors);
        Assertions.assertFalse(personalDoctors.isEmpty());
        Assertions.assertTrue(personalDoctors.get(0).getIsPersonalDoctor());
    }

    @Test
    public void testFindDoctorByUniqueId_ShouldReturnSpecificDoctor() {
        var doctor = doctorRepository.findDoctorByUniqueId("DOCTOR1");
        Assertions.assertTrue(doctor.isPresent());
        Assertions.assertEquals("DOCTOR1", doctor.get().getUniqueId());
    }

    @Test
    public void testFindDoctorSpecializations_ShouldReturnSpecializations() {
        var doctorSpecializations = doctorRepository.findDoctorSpecializations(testDoctor.getId());
        Assertions.assertNotNull(doctorSpecializations);
        Assertions.assertEquals(2, doctorSpecializations.size());
    }
}
