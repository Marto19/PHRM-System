package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.dto.DoctorSpecializationDTO;
import com.phrmSystem.phrmSystem.service.DoctorSpecializationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class DoctorSpecializationControllerTest {

    @Mock
    private DoctorSpecializationService doctorSpecializationService;

    private DoctorSpecializationController doctorSpecializationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doctorSpecializationController = new DoctorSpecializationController(doctorSpecializationService);
    }

    @Test
    void getDoctorSpecializationById_Success() {
        DoctorSpecializationDTO specializationDTO = new DoctorSpecializationDTO(1L, "Cardiology", null);

        when(doctorSpecializationService.getDoctorSpecializationById(1L)).thenReturn(specializationDTO);

        ResponseEntity<?> response = doctorSpecializationController.getDoctorSpecializationById(1L);

        assertEquals(OK, response.getStatusCode());
        assertEquals(specializationDTO, response.getBody());
        verify(doctorSpecializationService, times(1)).getDoctorSpecializationById(1L);
    }

    @Test
    void getDoctorSpecializationById_Failure_NotFound() {
        when(doctorSpecializationService.getDoctorSpecializationById(1L))
                .thenThrow(new RuntimeException("Doctor specialization not found with id: 1"));

        ResponseEntity<?> response = doctorSpecializationController.getDoctorSpecializationById(1L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Doctor specialization not found with id: 1", response.getBody());
        verify(doctorSpecializationService, times(1)).getDoctorSpecializationById(1L);
    }

    @Test
    void getAllDoctorSpecializations_Success() {
        DoctorSpecializationDTO specializationDTO = new DoctorSpecializationDTO(1L, "Cardiology", null);

        when(doctorSpecializationService.getAllDoctorSpecializations()).thenReturn(List.of(specializationDTO));

        ResponseEntity<List<DoctorSpecializationDTO>> response = doctorSpecializationController.getAllDoctorSpecializations();

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(doctorSpecializationService, times(1)).getAllDoctorSpecializations();
    }

    @Test
    void createDoctorSpecialization_Success() {
        DoctorSpecializationDTO specializationDTO = new DoctorSpecializationDTO(1L, "Cardiology", null);

        when(doctorSpecializationService.createDoctorSpecialization(specializationDTO)).thenReturn(specializationDTO);

        ResponseEntity<?> response = doctorSpecializationController.createDoctorSpecialization(specializationDTO);

        assertEquals(CREATED, response.getStatusCode());
        assertEquals(specializationDTO, response.getBody());
        verify(doctorSpecializationService, times(1)).createDoctorSpecialization(specializationDTO);
    }

    @Test
    void createDoctorSpecialization_Failure_Validation() {
        DoctorSpecializationDTO specializationDTO = new DoctorSpecializationDTO();

        when(doctorSpecializationService.createDoctorSpecialization(specializationDTO))
                .thenThrow(new RuntimeException("Specialization name cannot be null or blank."));

        ResponseEntity<?> response = doctorSpecializationController.createDoctorSpecialization(specializationDTO);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Specialization name cannot be null or blank.", response.getBody());
        verify(doctorSpecializationService, times(1)).createDoctorSpecialization(specializationDTO);
    }

    @Test
    void updateDoctorSpecialization_Success() {
        DoctorSpecializationDTO specializationDTO = new DoctorSpecializationDTO(1L, "Cardiology", null);

        when(doctorSpecializationService.updateDoctorSpecialization(1L, specializationDTO)).thenReturn(specializationDTO);

        ResponseEntity<?> response = doctorSpecializationController.updateDoctorSpecialization(1L, specializationDTO);

        assertEquals(OK, response.getStatusCode());
        assertEquals(specializationDTO, response.getBody());
        verify(doctorSpecializationService, times(1)).updateDoctorSpecialization(1L, specializationDTO);
    }

    @Test
    void updateDoctorSpecialization_Failure_NotFound() {
        DoctorSpecializationDTO specializationDTO = new DoctorSpecializationDTO();

        when(doctorSpecializationService.updateDoctorSpecialization(1L, specializationDTO))
                .thenThrow(new RuntimeException("Doctor specialization not found with id: 1"));

        ResponseEntity<?> response = doctorSpecializationController.updateDoctorSpecialization(1L, specializationDTO);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Doctor specialization not found with id: 1", response.getBody());
        verify(doctorSpecializationService, times(1)).updateDoctorSpecialization(1L, specializationDTO);
    }

    @Test
    void deleteDoctorSpecialization_Success() {
        doNothing().when(doctorSpecializationService).deleteDoctorSpecialization(1L);

        ResponseEntity<?> response = doctorSpecializationController.deleteDoctorSpecialization(1L);

        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(doctorSpecializationService, times(1)).deleteDoctorSpecialization(1L);
    }

    @Test
    void deleteDoctorSpecialization_Failure_NotFound() {
        doThrow(new RuntimeException("Doctor specialization not found with id: 1"))
                .when(doctorSpecializationService).deleteDoctorSpecialization(1L);

        ResponseEntity<?> response = doctorSpecializationController.deleteDoctorSpecialization(1L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Doctor specialization not found with id: 1", response.getBody());
        verify(doctorSpecializationService, times(1)).deleteDoctorSpecialization(1L);
    }
}
