package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.DoctorSpecialization;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.repo.DoctorSpecializationRepository;
import com.phrmSystem.phrmSystem.data.repo.UserRepository;
import com.phrmSystem.phrmSystem.dto.DoctorSpecializationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorSpecializationServiceImplTest {

    private DoctorSpecializationRepository specializationRepository;
    private UserRepository userRepository;
    private DoctorSpecializationServiceImpl specializationService;

    @BeforeEach
    void setUp() {
        specializationRepository = mock(DoctorSpecializationRepository.class);
        userRepository = mock(UserRepository.class);
        specializationService = new DoctorSpecializationServiceImpl(specializationRepository, userRepository);
    }

    @Test
    void getDoctorSpecializationById_Success() {
        DoctorSpecialization specialization = new DoctorSpecialization();
        specialization.setId(1L);
        specialization.setSpecialization("Cardiology");

        when(specializationRepository.findById(1L)).thenReturn(Optional.of(specialization));

        DoctorSpecializationDTO result = specializationService.getDoctorSpecializationById(1L);

        assertEquals("Cardiology", result.getSpecialization());
    }

    @Test
    void getDoctorSpecializationById_Failure_NotFound() {
        when(specializationRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> specializationService.getDoctorSpecializationById(1L));
        assertEquals("Doctor specialization not found with id: 1", exception.getMessage());
    }

    @Test
    void createDoctorSpecialization_Success() {
        DoctorSpecializationDTO specializationDTO = new DoctorSpecializationDTO(null, "Neurology", Set.of(1L, 2L));

        User doctor1 = new User();
        doctor1.setId(1L);
        User doctor2 = new User();
        doctor2.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(doctor1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(doctor2));

        DoctorSpecialization specialization = new DoctorSpecialization();
        specialization.setId(1L);
        specialization.setSpecialization("Neurology");
        specialization.setDoctors(Set.of(doctor1, doctor2));

        when(specializationRepository.save(any(DoctorSpecialization.class))).thenReturn(specialization);

        DoctorSpecializationDTO result = specializationService.createDoctorSpecialization(specializationDTO);

        assertEquals("Neurology", result.getSpecialization());
        assertEquals(2, result.getDoctorIds().size());
        verify(specializationRepository, times(1)).save(any(DoctorSpecialization.class));
    }

    @Test
    void createDoctorSpecialization_Failure_DoctorNotFound() {
        DoctorSpecializationDTO specializationDTO = new DoctorSpecializationDTO(null, "Neurology", Set.of(1L));

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> specializationService.createDoctorSpecialization(specializationDTO));
        assertEquals("Doctor not found with id: 1", exception.getMessage());
    }

    @Test
    void updateDoctorSpecialization_Success() {
        DoctorSpecialization existingSpecialization = new DoctorSpecialization();
        existingSpecialization.setId(1L);
        existingSpecialization.setSpecialization("Cardiology");

        DoctorSpecializationDTO updatedDTO = new DoctorSpecializationDTO(1L, "Neurology", Set.of(2L));
        User doctor = new User();
        doctor.setId(2L);

        when(specializationRepository.findById(1L)).thenReturn(Optional.of(existingSpecialization));
        when(userRepository.findById(2L)).thenReturn(Optional.of(doctor));

        DoctorSpecialization updatedSpecialization = new DoctorSpecialization();
        updatedSpecialization.setId(1L);
        updatedSpecialization.setSpecialization("Neurology");
        updatedSpecialization.setDoctors(Set.of(doctor));

        when(specializationRepository.save(existingSpecialization)).thenReturn(updatedSpecialization);

        DoctorSpecializationDTO result = specializationService.updateDoctorSpecialization(1L, updatedDTO);

        assertEquals("Neurology", result.getSpecialization());
        assertEquals(1, result.getDoctorIds().size());
        verify(specializationRepository, times(1)).save(existingSpecialization);
    }

    @Test
    void updateDoctorSpecialization_Failure_NotFound() {
        DoctorSpecializationDTO updatedDTO = new DoctorSpecializationDTO(1L, "Neurology", Set.of(2L));

        when(specializationRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> specializationService.updateDoctorSpecialization(1L, updatedDTO));
        assertEquals("Doctor specialization not found with id: 1", exception.getMessage());
    }

    @Test
    void deleteDoctorSpecialization_Success() {
        DoctorSpecialization specialization = new DoctorSpecialization();
        specialization.setId(1L);
        specialization.setDoctors(Set.of());

        when(specializationRepository.findById(1L)).thenReturn(Optional.of(specialization));

        assertDoesNotThrow(() -> specializationService.deleteDoctorSpecialization(1L));
        verify(specializationRepository, times(1)).delete(specialization);
    }

    @Test
    void deleteDoctorSpecialization_Failure_WithDoctors() {
        DoctorSpecialization specialization = new DoctorSpecialization();
        specialization.setId(1L);
        User doctor = new User();
        doctor.setId(1L);
        specialization.setDoctors(Set.of(doctor));

        when(specializationRepository.findById(1L)).thenReturn(Optional.of(specialization));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> specializationService.deleteDoctorSpecialization(1L));
        assertEquals("Cannot delete specialization as it is assigned to doctors.", exception.getMessage());
    }

    @Test
    void deleteDoctorSpecialization_Failure_NotFound() {
        when(specializationRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> specializationService.deleteDoctorSpecialization(1L));
        assertEquals("Doctor specialization not found with id: 1", exception.getMessage());
    }
}
