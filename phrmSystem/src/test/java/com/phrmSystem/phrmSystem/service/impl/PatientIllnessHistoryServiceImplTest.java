package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.PatientIllnessHistory;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.repo.PatientIllnessHistoryRepository;
import com.phrmSystem.phrmSystem.data.repo.UserRepository;
import com.phrmSystem.phrmSystem.dto.PatientIllnessHistoryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientIllnessHistoryServiceImplTest {

    private PatientIllnessHistoryRepository repository;
    private UserRepository userRepository;
    private PatientIllnessHistoryServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(PatientIllnessHistoryRepository.class);
        userRepository = mock(UserRepository.class);
        service = new PatientIllnessHistoryServiceImpl(repository, userRepository);
    }

    @Test
    void getAllIllnessHistories_Success() {
        PatientIllnessHistory history = new PatientIllnessHistory();
        history.setId(1L);
        history.setIllnessName("Flu");

        when(repository.findAll()).thenReturn(List.of(history));

        List<PatientIllnessHistoryDTO> result = service.getAllIllnessHistories();

        assertEquals(1, result.size());
        assertEquals("Flu", result.get(0).getIllnessName());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getIllnessHistoryById_Success() {
        PatientIllnessHistory history = new PatientIllnessHistory();
        history.setId(1L);
        history.setIllnessName("Flu");

        when(repository.findById(1L)).thenReturn(Optional.of(history));

        PatientIllnessHistoryDTO result = service.getIllnessHistoryById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Flu", result.getIllnessName());
    }

    @Test
    void getIllnessHistoryById_Failure_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.getIllnessHistoryById(1L));
        assertEquals("Illness history not found with id: 1", exception.getMessage());
    }

    @Test
    void getIllnessHistoriesByPatientId_Success() {
        PatientIllnessHistory history = new PatientIllnessHistory();
        history.setId(1L);
        history.setIllnessName("Flu");

        when(repository.findByPatientId(1L)).thenReturn(List.of(history));

        List<PatientIllnessHistoryDTO> result = service.getIllnessHistoriesByPatientId(1L);

        assertEquals(1, result.size());
        assertEquals("Flu", result.get(0).getIllnessName());
    }

    @Test
    void createIllnessHistory_Success() {
        User patient = new User();
        patient.setId(1L);

        PatientIllnessHistoryDTO dto = new PatientIllnessHistoryDTO(null, "Flu", LocalDate.now().minusDays(5), LocalDate.now(), 1L);

        PatientIllnessHistory history = new PatientIllnessHistory();
        history.setId(1L);
        history.setIllnessName("Flu");
        history.setPatient(patient);

        when(userRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(repository.save(any(PatientIllnessHistory.class))).thenReturn(history);

        PatientIllnessHistoryDTO result = service.createIllnessHistory(dto);

        assertNotNull(result);
        assertEquals("Flu", result.getIllnessName());
        assertEquals(1L, result.getPatientId());
    }

    @Test
    void createIllnessHistory_Failure_InvalidPatient() {
        PatientIllnessHistoryDTO dto = new PatientIllnessHistoryDTO(null, "Flu", LocalDate.now().minusDays(5), LocalDate.now(), 1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.createIllnessHistory(dto));
        assertEquals("Patient not found with id: 1", exception.getMessage());
    }

    @Test
    void updateIllnessHistory_Success() {
        User patient = new User();
        patient.setId(1L);

        PatientIllnessHistory existingHistory = new PatientIllnessHistory();
        existingHistory.setId(1L);
        existingHistory.setIllnessName("Flu");

        PatientIllnessHistoryDTO dto = new PatientIllnessHistoryDTO(1L, "Cold", LocalDate.now().minusDays(10), LocalDate.now(), 1L);

        when(repository.findById(1L)).thenReturn(Optional.of(existingHistory));
        when(userRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(repository.save(existingHistory)).thenReturn(existingHistory);

        PatientIllnessHistoryDTO result = service.updateIllnessHistory(1L, dto);

        assertEquals("Cold", result.getIllnessName());
        verify(repository, times(1)).save(existingHistory);
    }

    @Test
    void updateIllnessHistory_Failure_NotFound() {
        PatientIllnessHistoryDTO dto = new PatientIllnessHistoryDTO(1L, "Cold", LocalDate.now().minusDays(10), LocalDate.now(), 1L);

        when(repository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.updateIllnessHistory(1L, dto));
        assertEquals("Illness history not found with id: 1", exception.getMessage());
    }

    @Test
    void deleteIllnessHistory_Success() {
        when(repository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> service.deleteIllnessHistory(1L));
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void deleteIllnessHistory_Failure_NotFound() {
        when(repository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.deleteIllnessHistory(1L));
        assertEquals("Illness history not found with id: 1", exception.getMessage());
    }
}
