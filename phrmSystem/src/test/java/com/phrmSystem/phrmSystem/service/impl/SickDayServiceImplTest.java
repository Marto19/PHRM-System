package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.Diagnosis;
import com.phrmSystem.phrmSystem.data.entity.SickDay;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.repo.DiagnosisRepository;
import com.phrmSystem.phrmSystem.data.repo.SickDayRepository;
import com.phrmSystem.phrmSystem.data.repo.UserRepository;
import com.phrmSystem.phrmSystem.dto.SickDayDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SickDayServiceImplTest {

    private SickDayRepository sickDayRepository;
    private UserRepository userRepository;
    private DiagnosisRepository diagnosisRepository;
    private SickDayServiceImpl sickDayService;

    @BeforeEach
    void setUp() {
        sickDayRepository = mock(SickDayRepository.class);
        userRepository = mock(UserRepository.class);
        diagnosisRepository = mock(DiagnosisRepository.class);
        sickDayService = new SickDayServiceImpl(sickDayRepository, userRepository, diagnosisRepository);
    }

    @Test
    void getSickDayById_Success() {
        SickDay sickDay = new SickDay();
        sickDay.setId(1L);
        sickDay.setStartDate(LocalDate.now().minusDays(5));
        sickDay.setEndDate(LocalDate.now());

        when(sickDayRepository.findById(1L)).thenReturn(Optional.of(sickDay));

        SickDayDTO result = sickDayService.getSickDayById(1L);

        assertEquals(1L, result.getId());
        verify(sickDayRepository, times(1)).findById(1L);
    }

    @Test
    void getSickDayById_Failure_NotFound() {
        when(sickDayRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> sickDayService.getSickDayById(1L));
        assertEquals("SickDay not found with id: 1", exception.getMessage());
        verify(sickDayRepository, times(1)).findById(1L);
    }

    @Test
    void getAllSickDays_Success() {
        SickDay sickDay1 = new SickDay();
        sickDay1.setId(1L);
        SickDay sickDay2 = new SickDay();
        sickDay2.setId(2L);

        when(sickDayRepository.findAll()).thenReturn(List.of(sickDay1, sickDay2));

        List<SickDayDTO> result = sickDayService.getAllSickDays();

        assertEquals(2, result.size());
        verify(sickDayRepository, times(1)).findAll();
    }

    @Test
    void createSickDay_Success() {
        SickDayDTO sickDayDTO = new SickDayDTO();
        sickDayDTO.setStartDate(LocalDate.now().minusDays(5));
        sickDayDTO.setEndDate(LocalDate.now());
        sickDayDTO.setPatientId(1L);
        sickDayDTO.setDoctorId(2L);

        User patient = new User();
        patient.setId(1L);
        User doctor = new User();
        doctor.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(userRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(sickDayRepository.save(any(SickDay.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SickDayDTO result = sickDayService.createSickDay(sickDayDTO);

        assertEquals(5, result.getNumberOfDays());
        assertNotNull(result);
        verify(sickDayRepository, times(1)).save(any(SickDay.class));
    }

    @Test
    void createSickDay_Failure_InvalidDates() {
        SickDayDTO sickDayDTO = new SickDayDTO();
        sickDayDTO.setStartDate(LocalDate.now());
        sickDayDTO.setEndDate(LocalDate.now().minusDays(5));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> sickDayService.createSickDay(sickDayDTO));
        assertEquals("Start date cannot be after end date.", exception.getMessage());

        verify(sickDayRepository, never()).save(any(SickDay.class));
    }

    @Test
    void updateSickDay_Success() {
        SickDay sickDay = new SickDay();
        sickDay.setId(1L);

        SickDayDTO sickDayDTO = new SickDayDTO();
        sickDayDTO.setStartDate(LocalDate.now().minusDays(5));
        sickDayDTO.setEndDate(LocalDate.now());
        sickDayDTO.setPatientId(1L);
        sickDayDTO.setDoctorId(2L);

        User patient = new User();
        patient.setId(1L);
        User doctor = new User();
        doctor.setId(2L);

        when(sickDayRepository.findById(1L)).thenReturn(Optional.of(sickDay));
        when(userRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(userRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(sickDayRepository.save(any(SickDay.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SickDayDTO result = sickDayService.updateSickDay(1L, sickDayDTO);

        assertEquals(5, result.getNumberOfDays());
        assertNotNull(result);
        verify(sickDayRepository, times(1)).save(any(SickDay.class));
    }

    @Test
    void updateSickDay_Failure_NotFound() {
        SickDayDTO sickDayDTO = new SickDayDTO();
        sickDayDTO.setStartDate(LocalDate.now().minusDays(5));
        sickDayDTO.setEndDate(LocalDate.now());

        when(sickDayRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> sickDayService.updateSickDay(1L, sickDayDTO));
        assertEquals("SickDay not found with id: 1", exception.getMessage());

        verify(sickDayRepository, never()).save(any(SickDay.class));
    }

    @Test
    void deleteSickDay_Success() {
        SickDay sickDay = new SickDay();
        sickDay.setId(1L);

        when(sickDayRepository.findById(1L)).thenReturn(Optional.of(sickDay));

        assertDoesNotThrow(() -> sickDayService.deleteSickDay(1L));

        verify(sickDayRepository, times(1)).delete(sickDay);
    }

    @Test
    void deleteSickDay_Failure_NotFound() {
        when(sickDayRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> sickDayService.deleteSickDay(1L));
        assertEquals("SickDay not found with id: 1", exception.getMessage());

        verify(sickDayRepository, never()).delete(any(SickDay.class));
    }
}
