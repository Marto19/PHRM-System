package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.data.entity.Diagnosis;
import com.phrmSystem.phrmSystem.data.repo.DiagnosisRepository;
import com.phrmSystem.phrmSystem.dto.DiagnosisDTO;
import com.phrmSystem.phrmSystem.service.DiagnosisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class DiagnosisControllerTest {

    @Mock
    private DiagnosisService diagnosisService;

    @Mock
    private DiagnosisRepository diagnosisRepository;

    private DiagnosisController diagnosisController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        diagnosisController = new DiagnosisController(diagnosisService, diagnosisRepository);
    }

    @Test
    void createDiagnosis_Success() {
        DiagnosisDTO diagnosisDTO = new DiagnosisDTO(1L, "Flu", "Common flu symptoms", null, null, null);

        when(diagnosisService.createDiagnosis(diagnosisDTO)).thenReturn(diagnosisDTO);

        ResponseEntity<?> response = diagnosisController.createDiagnosis(diagnosisDTO);

        assertEquals(OK, response.getStatusCode());
        assertEquals(diagnosisDTO, response.getBody());
        verify(diagnosisService, times(1)).createDiagnosis(diagnosisDTO);
    }

    @Test
    void createDiagnosis_Failure() {
        DiagnosisDTO diagnosisDTO = new DiagnosisDTO();

        when(diagnosisService.createDiagnosis(diagnosisDTO))
                .thenThrow(new RuntimeException("Diagnosis name cannot be null or blank."));

        ResponseEntity<?> response = diagnosisController.createDiagnosis(diagnosisDTO);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Diagnosis name cannot be null or blank.", response.getBody());
        verify(diagnosisService, times(1)).createDiagnosis(diagnosisDTO);
    }

    @Test
    void updateDiagnosis_Success() {
        DiagnosisDTO diagnosisDTO = new DiagnosisDTO(1L, "Updated Flu", "Updated description", null, null, null);

        when(diagnosisService.updateDiagnosis(1L, diagnosisDTO)).thenReturn(diagnosisDTO);

        ResponseEntity<?> response = diagnosisController.updateDiagnosis(1L, diagnosisDTO);

        assertEquals(OK, response.getStatusCode());
        assertEquals(diagnosisDTO, response.getBody());
        verify(diagnosisService, times(1)).updateDiagnosis(1L, diagnosisDTO);
    }

    @Test
    void updateDiagnosis_Failure() {
        DiagnosisDTO diagnosisDTO = new DiagnosisDTO();

        when(diagnosisService.updateDiagnosis(1L, diagnosisDTO))
                .thenThrow(new RuntimeException("Diagnosis not found with ID: 1"));

        ResponseEntity<?> response = diagnosisController.updateDiagnosis(1L, diagnosisDTO);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Diagnosis not found with ID: 1", response.getBody());
        verify(diagnosisService, times(1)).updateDiagnosis(1L, diagnosisDTO);
    }

    @Test
    void deleteDiagnosis_Success() {
        doNothing().when(diagnosisService).deleteDiagnosis(1L);

        ResponseEntity<?> response = diagnosisController.deleteDiagnosis(1L);

        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(diagnosisService, times(1)).deleteDiagnosis(1L);
    }

    @Test
    void deleteDiagnosis_Failure() {
        doThrow(new RuntimeException("Diagnosis not found with ID: 1")).when(diagnosisService).deleteDiagnosis(1L);

        ResponseEntity<?> response = diagnosisController.deleteDiagnosis(1L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Diagnosis not found with ID: 1", response.getBody());
        verify(diagnosisService, times(1)).deleteDiagnosis(1L);
    }

    @Test
    void getAllDiagnoses_Success() {
        DiagnosisDTO diagnosisDTO = new DiagnosisDTO(1L, "Flu", "Common flu symptoms", null, null, null);

        when(diagnosisService.getAllDiagnoses()).thenReturn(List.of(diagnosisDTO));

        ResponseEntity<List<DiagnosisDTO>> response = diagnosisController.getAllDiagnoses();

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(diagnosisService, times(1)).getAllDiagnoses();
    }

    @Test
    void getDiagnosisById_Success() {
        DiagnosisDTO diagnosisDTO = new DiagnosisDTO(1L, "Flu", "Common flu symptoms", null, null, null);

        when(diagnosisService.getDiagnosisById(1L)).thenReturn(diagnosisDTO);

        ResponseEntity<?> response = diagnosisController.getDiagnosisById(1L);

        assertEquals(OK, response.getStatusCode());
        assertEquals(diagnosisDTO, response.getBody());
        verify(diagnosisService, times(1)).getDiagnosisById(1L);
    }

    @Test
    void getDiagnosisById_Failure() {
        when(diagnosisService.getDiagnosisById(1L)).thenThrow(new RuntimeException("Diagnosis not found with ID: 1"));

        ResponseEntity<?> response = diagnosisController.getDiagnosisById(1L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Diagnosis not found with ID: 1", response.getBody());
        verify(diagnosisService, times(1)).getDiagnosisById(1L);
    }

    @Test
    void getMostCommonDiagnoses_Success() {
        Object[] mockResult = new Object[]{"Flu", 10L};
        when(diagnosisRepository.findMostCommonDiagnoses()).thenReturn(List.<Object[]>of((Object[]) mockResult)); // Explicit cast

        ResponseEntity<?> response = diagnosisController.getMostCommonDiagnoses();

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
        verify(diagnosisRepository, times(1)).findMostCommonDiagnoses();
    }


    @Test
    void getDiagnosesByName_Success() {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setId(1L);
        diagnosis.setDiagnosisName("Flu");

        when(diagnosisRepository.findByDiagnosisName("Flu")).thenReturn(List.of(diagnosis));

        ResponseEntity<?> response = diagnosisController.getDiagnosesByName("Flu");

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
        verify(diagnosisRepository, times(1)).findByDiagnosisName("Flu");
    }

    @Test
    void getDiagnosesByName_Failure_EmptyInput() {
        ResponseEntity<?> response = diagnosisController.getDiagnosesByName("");

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Diagnosis name cannot be null or empty.", response.getBody());
        verify(diagnosisRepository, never()).findByDiagnosisName(anyString());
    }

    @Test
    void getDiagnosesByName_NoResults() {
        when(diagnosisRepository.findByDiagnosisName("Unknown")).thenReturn(List.of());

        ResponseEntity<?> response = diagnosisController.getDiagnosesByName("Unknown");

        assertEquals(OK, response.getStatusCode());
        assertEquals("No diagnoses found matching the name: Unknown", response.getBody());
        verify(diagnosisRepository, times(1)).findByDiagnosisName("Unknown");
    }
}
