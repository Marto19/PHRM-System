package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.Role;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.entity.PatientIllnessHistory;
import com.phrmSystem.phrmSystem.data.entity.DoctorAppointment;
import com.phrmSystem.phrmSystem.data.repo.PatientRepository;
import com.phrmSystem.phrmSystem.data.repo.DoctorAppointmentRepository;
import com.phrmSystem.phrmSystem.data.repo.RoleRepository;
import com.phrmSystem.phrmSystem.data.repo.UserRepository;
import com.phrmSystem.phrmSystem.dto.DoctorAppointmentDTO;
import com.phrmSystem.phrmSystem.dto.PatientIllnessHistoryDTO;
import com.phrmSystem.phrmSystem.dto.UserDTO;
import com.phrmSystem.phrmSystem.mappers.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceImplTest {

    private PatientRepository patientRepository;
    private DoctorAppointmentRepository doctorAppointmentRepository;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private PatientServiceImpl patientService;

    @BeforeEach
    void setUp() {
        patientRepository = mock(PatientRepository.class);
        doctorAppointmentRepository = mock(DoctorAppointmentRepository.class);
        roleRepository = mock(RoleRepository.class);
        userRepository = mock(UserRepository.class);
        patientService = new PatientServiceImpl(patientRepository, doctorAppointmentRepository, roleRepository, userRepository);
    }

    @Test
    void createPatient_Success() {
        User patient = new User();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setUniqueIdentification("12345");
        Role patientRole = new Role();
        patientRole.setRoleName("PATIENT");
        patient.setRole(List.of(patientRole));

        when(roleRepository.findByRoleName("PATIENT")).thenReturn(Optional.of(patientRole));
        when(userRepository.save(patient)).thenReturn(patient);

        User result = patientService.createPatient(patient);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(userRepository, times(1)).save(patient);
    }

    @Test
    void createPatient_Failure_InvalidRole() {
        User patient = new User();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setUniqueIdentification("12345");
        patient.setRole(List.of()); // No roles assigned

        RuntimeException exception = assertThrows(RuntimeException.class, () -> patientService.createPatient(patient));
        assertEquals("User must have a role of PATIENT.", exception.getMessage());
    }

    @Test
    void updatePatient_Success() {
        User existingPatient = new User();
        existingPatient.setId(1L);
        existingPatient.setFirstName("John");

        Role patientRole = new Role();
        patientRole.setRoleName("PATIENT");

        User updatedPatient = new User();
        updatedPatient.setFirstName("Jane");
        updatedPatient.setRole(List.of(patientRole));
        updatedPatient.setUniqueIdentification("12345"); // Add uniqueIdentification

        when(patientRepository.findById(1L)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(existingPatient)).thenReturn(existingPatient);

        User result = patientService.updatePatient(1L, updatedPatient);

        assertEquals("Jane", result.getFirstName());
        verify(patientRepository, times(1)).save(existingPatient);
    }


    @Test
    void updatePatient_Failure_NotFound() {
        User updatedPatient = new User();
        updatedPatient.setRole(List.of(new Role("PATIENT")));

        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> patientService.updatePatient(1L, updatedPatient));
        assertEquals("Patient not found with ID: 1", exception.getMessage());
    }

    @Test
    void deletePatient_Success() {
        User patient = new User();
        patient.setId(1L);
        patient.setDoctorAppointment(Set.of());
        patient.setPatientIllnessHistory(Set.of());

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        assertDoesNotThrow(() -> patientService.deletePatient(1L));
        verify(patientRepository, times(1)).deleteById(1L);
        verify(doctorAppointmentRepository, never()).deleteAll(any());
    }

    @Test
    void deletePatient_WithDoctorAppointments() {
        User patient = new User();
        patient.setId(1L);

        DoctorAppointment appointment = new DoctorAppointment();
        appointment.setId(1L);

        // Add the appointment to the patient's doctorAppointment set
        patient.setDoctorAppointment(Set.of(appointment));

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> patientService.deletePatient(1L));
        assertEquals("Cannot delete patient with active doctor appointments. Please cancel or reassign the appointments first.", exception.getMessage());

        // Verify that the deleteById method is not called
        verify(patientRepository, never()).deleteById(any());
    }


    @Test
    void deletePatient_Failure_WithIllnessHistories() {
        User patient = new User();
        patient.setId(1L);
        patient.setPatientIllnessHistory(Set.of(new PatientIllnessHistory()));

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> patientService.deletePatient(1L));
        assertEquals("Cannot delete patient with active illness histories. Please delete or reassign the histories first.", exception.getMessage());
        verify(patientRepository, never()).deleteById(any());
    }

    @Test
    void deletePatient_Failure_WithAppointments() {
        User patient = new User();
        patient.setId(1L);

        DoctorAppointment appointment = new DoctorAppointment();
        appointment.setId(1L);

        // Add the appointment to the patient's doctorAppointment set
        patient.setDoctorAppointment(Set.of(appointment));

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> patientService.deletePatient(1L));
        assertEquals("Cannot delete patient with active doctor appointments. Please cancel or reassign the appointments first.", exception.getMessage());

        // Ensure the repository's delete method is not called
        verify(patientRepository, never()).deleteById(any());
    }


    @Test
    void getAllPatients_Success() {
        User patient = new User();
        patient.setFirstName("John");

        when(patientRepository.findAllUsersByRoleName("PATIENT")).thenReturn(List.of(patient));

        List<UserDTO> result = patientService.getAllPatients();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    void getPatientById_Success() {
        User patient = new User();
        patient.setId(1L);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        UserDTO result = patientService.getPatientById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getPatientById_Failure_NotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> patientService.getPatientById(1L));
        assertEquals("Patient not found with id: 1", exception.getMessage());
    }

    @Test
    void getPatientIllnessHistory_Success() {
        User patient = new User();
        patient.setId(1L);
        PatientIllnessHistory history = new PatientIllnessHistory();
        history.setIllnessName("Flu");
        history.setPatient(patient); // Ensure the relationship is set
        patient.setPatientIllnessHistory(Set.of(history));

        when(userRepository.findById(1L)).thenReturn(Optional.of(patient));

        List<PatientIllnessHistoryDTO> result = patientService.getPatientIllnessHistory(1L);

        assertEquals(1, result.size());
        assertEquals("Flu", result.get(0).getIllnessName());
        assertEquals(1L, result.get(0).getPatientId());
    }


    @Test
    void createAppointment_Success() {
        User patient = new User();
        patient.setId(1L);
        DoctorAppointmentDTO appointmentDTO = new DoctorAppointmentDTO(null, LocalDate.now().atStartOfDay(), 1L, null);
        DoctorAppointment appointment = new DoctorAppointment();
        appointment.setId(1L);
        appointment.setDate(LocalDate.now().atStartOfDay());
        appointment.setPatient(patient);

        when(userRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorAppointmentRepository.save(any(DoctorAppointment.class))).thenReturn(appointment);

        DoctorAppointmentDTO result = patientService.createAppointment(1L, appointmentDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }
}
