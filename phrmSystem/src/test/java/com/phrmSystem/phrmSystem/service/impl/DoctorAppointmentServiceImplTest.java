package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.*;
import com.phrmSystem.phrmSystem.data.repo.*;
import com.phrmSystem.phrmSystem.dto.DoctorAppointmentAllDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorAppointmentServiceImplTest {

    private DoctorAppointmentRepository doctorAppointmentRepository;
    private UserRepository userRepository;
    private PatientIllnessHistoryRepository patientIllnessHistoryRepository;
    private DiagnosisRepository diagnosisRepository;
    private DoctorAppointmentServiceImpl doctorAppointmentService;

    @BeforeEach
    void setUp() {
        doctorAppointmentRepository = mock(DoctorAppointmentRepository.class);
        userRepository = mock(UserRepository.class);
        patientIllnessHistoryRepository = mock(PatientIllnessHistoryRepository.class);
        diagnosisRepository = mock(DiagnosisRepository.class);
        doctorAppointmentService = new DoctorAppointmentServiceImpl(
                doctorAppointmentRepository,
                userRepository,
                patientIllnessHistoryRepository,
                diagnosisRepository
        );
    }

    @Test
    void createDoctorAppointment_Success() {
        DoctorAppointmentAllDTO appointmentDTO = new DoctorAppointmentAllDTO(
                null, LocalDateTime.now(), 1L, 2L, null, null
        );

        User patient = new User();
        patient.setId(1L);

        User doctor = new User();
        doctor.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(userRepository.findById(2L)).thenReturn(Optional.of(doctor));

        DoctorAppointment appointment = new DoctorAppointment();
        appointment.setId(1L);
        appointment.setDate(appointmentDTO.getDate());
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        when(doctorAppointmentRepository.save(any(DoctorAppointment.class))).thenReturn(appointment);

        DoctorAppointmentAllDTO result = doctorAppointmentService.createDoctorAppointment(appointmentDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(doctorAppointmentRepository, times(1)).save(any(DoctorAppointment.class));
    }

    @Test
    void createDoctorAppointment_Failure_MissingPatient() {
        DoctorAppointmentAllDTO appointmentDTO = new DoctorAppointmentAllDTO(
                null, LocalDateTime.now(), null, 2L, null, null
        );

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> doctorAppointmentService.createDoctorAppointment(appointmentDTO));
        assertEquals("Patient ID must be provided.", exception.getMessage());
    }

    @Test
    void updateDoctorAppointment_Success() {
        DoctorAppointment existingAppointment = new DoctorAppointment();
        existingAppointment.setId(1L);
        existingAppointment.setDate(LocalDateTime.now());

        DoctorAppointmentAllDTO updatedDTO = new DoctorAppointmentAllDTO(
                1L, LocalDateTime.now().plusDays(1), 1L, 2L, null, null
        );

        User patient = new User();
        patient.setId(1L);

        User doctor = new User();
        doctor.setId(2L);

        when(doctorAppointmentRepository.findById(1L)).thenReturn(Optional.of(existingAppointment));
        when(userRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(userRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(doctorAppointmentRepository.save(existingAppointment)).thenReturn(existingAppointment);

        DoctorAppointmentAllDTO result = doctorAppointmentService.updateDoctorAppointment(1L, updatedDTO);

        assertNotNull(result);
        verify(doctorAppointmentRepository, times(1)).save(existingAppointment);
    }

    @Test
    void updateDoctorAppointment_Failure_NotFound() {
        DoctorAppointmentAllDTO updatedDTO = new DoctorAppointmentAllDTO(
                1L, LocalDateTime.now().plusDays(1), 1L, 2L, null, null
        );

        when(doctorAppointmentRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> doctorAppointmentService.updateDoctorAppointment(1L, updatedDTO));
        assertEquals("Doctor Appointment not found with ID: 1", exception.getMessage());
    }

    @Test
    void getDoctorAppointmentById_Success() {
        DoctorAppointment appointment = new DoctorAppointment();
        appointment.setId(1L);
        appointment.setDate(LocalDateTime.now());

        when(doctorAppointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        DoctorAppointmentAllDTO result = doctorAppointmentService.getDoctorAppointmentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getDoctorAppointmentById_Failure_NotFound() {
        when(doctorAppointmentRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> doctorAppointmentService.getDoctorAppointmentById(1L));
        assertEquals("Doctor Appointment not found with ID: 1", exception.getMessage());
    }

    @Test
    void getAllDoctorAppointments_Success() {
        DoctorAppointment appointment1 = new DoctorAppointment();
        appointment1.setId(1L);

        DoctorAppointment appointment2 = new DoctorAppointment();
        appointment2.setId(2L);

        when(doctorAppointmentRepository.findAll()).thenReturn(List.of(appointment1, appointment2));

        List<DoctorAppointmentAllDTO> result = doctorAppointmentService.getAllDoctorAppointments();

        assertEquals(2, result.size());
        verify(doctorAppointmentRepository, times(1)).findAll();
    }

    @Test
    void deleteDoctorAppointment_Success() {
        when(doctorAppointmentRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> doctorAppointmentService.deleteDoctorAppointment(1L));
        verify(doctorAppointmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteDoctorAppointment_Failure_NotFound() {
        when(doctorAppointmentRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> doctorAppointmentService.deleteDoctorAppointment(1L));
        assertEquals("Doctor Appointment not found with ID: 1", exception.getMessage());
    }
}
