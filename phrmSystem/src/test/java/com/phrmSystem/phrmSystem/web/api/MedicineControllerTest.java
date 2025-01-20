package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.dto.MedicineDTO;
import com.phrmSystem.phrmSystem.service.MedicineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class MedicineControllerTest {

    @Mock
    private MedicineService medicineService;

    private MedicineController medicineController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        medicineController = new MedicineController(medicineService);
    }

    @Test
    void getMedicineById_Success() {
        MedicineDTO medicineDTO = new MedicineDTO(1L, "Aspirin", "Pain reliever", 2L);

        when(medicineService.getMedicineById(1L)).thenReturn(medicineDTO);

        ResponseEntity<?> response = medicineController.getMedicineById(1L);

        assertEquals(OK, response.getStatusCode());
        assertEquals(medicineDTO, response.getBody());
        verify(medicineService, times(1)).getMedicineById(1L);
    }

    @Test
    void getMedicineById_NotFound() {
        when(medicineService.getMedicineById(1L))
                .thenThrow(new RuntimeException("Medicine not found with id: 1"));

        ResponseEntity<?> response = medicineController.getMedicineById(1L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Medicine not found with id: 1", response.getBody());
        verify(medicineService, times(1)).getMedicineById(1L);
    }

    @Test
    void getAllMedicines_Success() {
        MedicineDTO medicineDTO = new MedicineDTO(1L, "Aspirin", "Pain reliever", 2L);

        when(medicineService.getAllMedicines()).thenReturn(List.of(medicineDTO));

        ResponseEntity<List<MedicineDTO>> response = medicineController.getAllMedicines();

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(medicineService, times(1)).getAllMedicines();
    }

    @Test
    void createMedicine_Success() {
        MedicineDTO medicineDTO = new MedicineDTO(null, "Aspirin", "Pain reliever", 2L);
        MedicineDTO createdDTO = new MedicineDTO(1L, "Aspirin", "Pain reliever", 2L);

        when(medicineService.createMedicine(medicineDTO)).thenReturn(createdDTO);

        ResponseEntity<?> response = medicineController.createMedicine(medicineDTO);

        assertEquals(CREATED, response.getStatusCode());
        assertEquals(createdDTO, response.getBody());
        verify(medicineService, times(1)).createMedicine(medicineDTO);
    }

    @Test
    void createMedicine_Failure() {
        MedicineDTO medicineDTO = new MedicineDTO();

        when(medicineService.createMedicine(medicineDTO))
                .thenThrow(new IllegalArgumentException("Invalid medicine data."));

        ResponseEntity<?> response = medicineController.createMedicine(medicineDTO);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid medicine data.", response.getBody());
        verify(medicineService, times(1)).createMedicine(medicineDTO);
    }

    @Test
    void updateMedicine_Success() {
        MedicineDTO medicineDTO = new MedicineDTO(1L, "Ibuprofen", "Anti-inflammatory", 2L);

        when(medicineService.updateMedicine(1L, medicineDTO)).thenReturn(medicineDTO);

        ResponseEntity<?> response = medicineController.updateMedicine(1L, medicineDTO);

        assertEquals(OK, response.getStatusCode());
        assertEquals(medicineDTO, response.getBody());
        verify(medicineService, times(1)).updateMedicine(1L, medicineDTO);
    }

    @Test
    void updateMedicine_Failure() {
        MedicineDTO medicineDTO = new MedicineDTO();

        when(medicineService.updateMedicine(1L, medicineDTO))
                .thenThrow(new RuntimeException("Medicine not found with id: 1"));

        ResponseEntity<?> response = medicineController.updateMedicine(1L, medicineDTO);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Medicine not found with id: 1", response.getBody());
        verify(medicineService, times(1)).updateMedicine(1L, medicineDTO);
    }

    @Test
    void deleteMedicine_Success() {
        doNothing().when(medicineService).deleteMedicine(1L);

        ResponseEntity<?> response = medicineController.deleteMedicine(1L);

        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(medicineService, times(1)).deleteMedicine(1L);
    }

    @Test
    void deleteMedicine_Failure() {
        doThrow(new RuntimeException("Medicine not found with id: 1"))
                .when(medicineService).deleteMedicine(1L);

        ResponseEntity<?> response = medicineController.deleteMedicine(1L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Medicine not found with id: 1", response.getBody());
        verify(medicineService, times(1)).deleteMedicine(1L);
    }

    @Test
    void getMedicinesByDiagnosis_Success() {
        MedicineDTO medicineDTO = new MedicineDTO(1L, "Paracetamol", "Pain reliever", 3L);

        when(medicineService.getMedicinesByDiagnosis(3L)).thenReturn(List.of(medicineDTO));

        ResponseEntity<?> response = medicineController.getMedicinesByDiagnosis(3L);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(((List<?>) response.getBody()).contains(medicineDTO));
        verify(medicineService, times(1)).getMedicinesByDiagnosis(3L);
    }

    @Test
    void getMedicinesByDiagnosis_Failure() {
        when(medicineService.getMedicinesByDiagnosis(3L))
                .thenThrow(new RuntimeException("No medicines found for diagnosis ID: 3"));

        ResponseEntity<?> response = medicineController.getMedicinesByDiagnosis(3L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("No medicines found for diagnosis ID: 3", response.getBody());
        verify(medicineService, times(1)).getMedicinesByDiagnosis(3L);
    }
}
