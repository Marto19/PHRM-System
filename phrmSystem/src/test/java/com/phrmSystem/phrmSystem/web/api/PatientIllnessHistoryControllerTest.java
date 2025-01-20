package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.dto.PatientIllnessHistoryDTO;
import com.phrmSystem.phrmSystem.service.PatientIllnessHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class PatientIllnessHistoryControllerTest {

    @Mock
    private PatientIllnessHistoryService service;

    private PatientIllnessHistoryController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new PatientIllnessHistoryController(service);
    }

    @Test
    void getAllIllnessHistories_Success() {
        PatientIllnessHistoryDTO dto = new PatientIllnessHistoryDTO();
        dto.setId(1L);

        when(service.getAllIllnessHistories()).thenReturn(List.of(dto));

        ResponseEntity<List<PatientIllnessHistoryDTO>> response = controller.getAllIllnessHistories();

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(service, times(1)).getAllIllnessHistories();
    }

    @Test
    void getIllnessHistoryById_Success() {
        PatientIllnessHistoryDTO dto = new PatientIllnessHistoryDTO();
        dto.setId(1L);

        when(service.getIllnessHistoryById(1L)).thenReturn(dto);

        ResponseEntity<?> response = controller.getIllnessHistoryById(1L);

        assertEquals(OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(service, times(1)).getIllnessHistoryById(1L);
    }

    @Test
    void getIllnessHistoryById_Failure() {
        when(service.getIllnessHistoryById(1L)).thenThrow(new RuntimeException("Illness history not found."));

        ResponseEntity<?> response = controller.getIllnessHistoryById(1L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Illness history not found.", response.getBody());
        verify(service, times(1)).getIllnessHistoryById(1L);
    }

    @Test
    void getIllnessHistoriesByPatientId_Success() {
        PatientIllnessHistoryDTO dto = new PatientIllnessHistoryDTO();
        dto.setId(1L);

        when(service.getIllnessHistoriesByPatientId(1L)).thenReturn(List.of(dto));

        ResponseEntity<?> response = controller.getIllnessHistoriesByPatientId(1L);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(service, times(1)).getIllnessHistoriesByPatientId(1L);
    }

    @Test
    void createIllnessHistory_Success() {
        PatientIllnessHistoryDTO dto = new PatientIllnessHistoryDTO();
        dto.setId(1L);

        when(service.createIllnessHistory(dto)).thenReturn(dto);

        ResponseEntity<?> response = controller.createIllnessHistory(dto);

        assertEquals(CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(service, times(1)).createIllnessHistory(dto);
    }

    @Test
    void createIllnessHistory_Failure() {
        PatientIllnessHistoryDTO dto = new PatientIllnessHistoryDTO();

        when(service.createIllnessHistory(dto)).thenThrow(new IllegalArgumentException("Validation failed."));

        ResponseEntity<?> response = controller.createIllnessHistory(dto);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed.", response.getBody());
        verify(service, times(1)).createIllnessHistory(dto);
    }

    @Test
    void updateIllnessHistory_Success() {
        PatientIllnessHistoryDTO dto = new PatientIllnessHistoryDTO();
        dto.setId(1L);

        when(service.updateIllnessHistory(1L, dto)).thenReturn(dto);

        ResponseEntity<?> response = controller.updateIllnessHistory(1L, dto);

        assertEquals(OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(service, times(1)).updateIllnessHistory(1L, dto);
    }

    @Test
    void deleteIllnessHistory_Success() {
        doNothing().when(service).deleteIllnessHistory(1L);

        ResponseEntity<?> response = controller.deleteIllnessHistory(1L);

        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).deleteIllnessHistory(1L);
    }

    @Test
    void deleteIllnessHistory_Failure() {
        doThrow(new RuntimeException("Illness history not found.")).when(service).deleteIllnessHistory(1L);

        ResponseEntity<?> response = controller.deleteIllnessHistory(1L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Illness history not found.", response.getBody());
        verify(service, times(1)).deleteIllnessHistory(1L);
    }
}
