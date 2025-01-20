package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.DoctorAppointment;
import com.phrmSystem.phrmSystem.data.entity.DoctorSpecialization;
import com.phrmSystem.phrmSystem.data.entity.Role;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.repo.DoctorRepository;
import com.phrmSystem.phrmSystem.data.repo.RoleRepository;
import com.phrmSystem.phrmSystem.dto.AppointmentDTO;
import com.phrmSystem.phrmSystem.dto.SpecializationDTO;
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

class DoctorServiceImplTest {

    private DoctorRepository doctorRepository;
    private RoleRepository roleRepository;
    private DoctorServiceImpl doctorService;

    @BeforeEach
    void setUp() {
        doctorRepository = mock(DoctorRepository.class);
        roleRepository = mock(RoleRepository.class);
        doctorService = new DoctorServiceImpl(doctorRepository, roleRepository);
    }

    @Test
    void createDoctor_Success() {
        User doctor = new User();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setUniqueId("D12345");
        doctor.setIsPersonalDoctor(true);

        Role doctorRole = new Role();
        doctorRole.setRoleName("DOCTOR");

        when(roleRepository.findByRoleName("DOCTOR")).thenReturn(Optional.of(doctorRole));
        when(doctorRepository.save(any(User.class))).thenReturn(doctor);

        User result = doctorService.createDoctor(doctor);

        assertEquals("John", result.getFirstName());
        assertEquals("DOCTOR", result.getRole().get(0).getRoleName());
        verify(doctorRepository, times(1)).save(doctor);
    }

    @Test
    void createDoctor_Failure_MissingRole() {
        User doctor = new User();
        doctor.setFirstName("John");
        doctor.setUniqueId("D12345");
        doctor.setIsPersonalDoctor(true);

        // Simulate no "DOCTOR" role found
        when(roleRepository.findByRoleName("DOCTOR")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> doctorService.createDoctor(doctor));
        assertEquals("Role DOCTOR not found.", exception.getMessage());
    }

    @Test
    void updateDoctor_Success() {
        User existingDoctor = new User();
        existingDoctor.setId(1L);
        existingDoctor.setFirstName("John");

        User updatedDoctor = new User();
        updatedDoctor.setFirstName("Jane");
        updatedDoctor.setUniqueId("D12345");
        updatedDoctor.setIsPersonalDoctor(true);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(existingDoctor));
        when(doctorRepository.save(existingDoctor)).thenReturn(existingDoctor);

        User result = doctorService.updateDoctor(1L, updatedDoctor);

        assertEquals("Jane", result.getFirstName());
        verify(doctorRepository, times(1)).save(existingDoctor);
    }

    @Test
    void updateDoctor_Failure_NotFound() {
        User updatedDoctor = new User();
        updatedDoctor.setFirstName("Jane");

        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> doctorService.updateDoctor(1L, updatedDoctor));
        assertEquals("Doctor not found with ID: 1", exception.getMessage());
    }

    @Test
    void deleteDoctor_Success() {
        User doctor = new User();
        doctor.setId(1L);
        doctor.setDoctorAppointments(Set.of());

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        assertDoesNotThrow(() -> doctorService.deleteDoctor(1L));
        verify(doctorRepository, times(1)).deleteById(1L);
    }


    @Test
    void deleteDoctor_Failure_WithAppointments() {
        User doctor = new User();
        doctor.setId(1L);
        doctor.setDoctorAppointments(Set.of(mock(DoctorAppointment.class)));

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> doctorService.deleteDoctor(1L));
        assertEquals("Cannot delete doctor with active appointments. Please reassign or cancel appointments first.", exception.getMessage());
    }

    @Test
    void deleteDoctor_Failure_DatabaseConstraint() {
        User doctor = new User();
        doctor.setId(1L);
        doctor.setDoctorAppointments(Set.of());

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        doThrow(new RuntimeException("constraint")).when(doctorRepository).deleteById(1L);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> doctorService.deleteDoctor(1L));
        assertEquals("Cannot delete doctor due to active dependencies. Ensure all references are removed.", exception.getMessage());
    }

    @Test
    void getDoctorById_Success() {
        User doctor = new User();
        doctor.setId(1L);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        UserDTO result = doctorService.getDoctorById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getDoctorById_Failure_NotFound() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> doctorService.getDoctorById(1L));
        assertEquals("Doctor not found with id: 1", exception.getMessage());
    }

    @Test
    void getDoctorAppointments_Success() {
        User doctor = new User();
        doctor.setId(1L);

        User patient = new User();
        patient.setFirstName("John");
        patient.setLastName("Doe");

        DoctorAppointment appointment = new DoctorAppointment();
        appointment.setId(1L);
        appointment.setPatient(patient);
        appointment.setDate(LocalDate.now().atStartOfDay());

        doctor.setDoctorAppointments(Set.of(appointment));

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        List<AppointmentDTO> result = doctorService.getDoctorAppointments(1L);

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getPatientName());
        verify(doctorRepository, times(1)).findById(1L);
    }

    @Test
    void getDoctorAppointments_Failure_NotFound() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> doctorService.getDoctorAppointments(1L));
        assertEquals("Doctor not found with id: 1", exception.getMessage());
    }

    @Test
    void addSpecializationToDoctor_Success() {
        User doctor = new User();
        doctor.setId(1L);

        SpecializationDTO specializationDTO = new SpecializationDTO(null, "Cardiology");

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(doctor)).thenReturn(doctor);

        UserDTO result = doctorService.addSpecializationToDoctor(1L, specializationDTO);

        assertNotNull(result);
        verify(doctorRepository, times(1)).save(doctor);
    }
}
