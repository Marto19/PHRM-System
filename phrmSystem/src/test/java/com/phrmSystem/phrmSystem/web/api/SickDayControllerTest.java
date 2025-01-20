package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.dto.SickDayDTO;
import com.phrmSystem.phrmSystem.service.SickDayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class SickDayControllerTest {

    @Mock
    private SickDayService sickDayService;

    private SickDayController sickDayController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sickDayController = new SickDayController(sickDayService);
    }

    @Test
    void getSickDayById_Success() {
        SickDayDTO sickDayDTO = new SickDayDTO();
        sickDayDTO.setId(1L);

        when(sickDayService.getSickDayById(1L)).thenReturn(sickDayDTO);

        ResponseEntity<?> response = sickDayController.getSickDayById(1L);

        assertEquals(OK, response.getStatusCode());
        assertEquals(sickDayDTO, response.getBody());
        verify(sickDayService, times(1)).getSickDayById(1L);
    }

    @Test
    void getSickDayById_Failure_NotFound() {
        when(sickDayService.getSickDayById(1L)).thenThrow(new RuntimeException("SickDay not found with id: 1"));

        ResponseEntity<?> response = sickDayController.getSickDayById(1L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("SickDay not found with id: 1", response.getBody());
        verify(sickDayService, times(1)).getSickDayById(1L);
    }

    @Test
    void getAllSickDays_Success() {
        SickDayDTO sickDayDTO = new SickDayDTO();
        sickDayDTO.setId(1L);

        when(sickDayService.getAllSickDays()).thenReturn(List.of(sickDayDTO));

        ResponseEntity<List<SickDayDTO>> response = sickDayController.getAllSickDays();

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(sickDayService, times(1)).getAllSickDays();
    }

    @Test
    void createSickDay_Success() {
        SickDayDTO sickDayDTO = new SickDayDTO();
        sickDayDTO.setId(1L);

        when(sickDayService.createSickDay(sickDayDTO)).thenReturn(sickDayDTO);

        ResponseEntity<?> response = sickDayController.createSickDay(sickDayDTO);

        assertEquals(CREATED, response.getStatusCode());
        assertEquals(sickDayDTO, response.getBody());
        verify(sickDayService, times(1)).createSickDay(sickDayDTO);
    }

    @Test
    void createSickDay_Failure() {
        SickDayDTO sickDayDTO = new SickDayDTO();

        when(sickDayService.createSickDay(sickDayDTO)).thenThrow(new IllegalArgumentException("Validation failed."));

        ResponseEntity<?> response = sickDayController.createSickDay(sickDayDTO);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed.", response.getBody());
        verify(sickDayService, times(1)).createSickDay(sickDayDTO);
    }
    @Test
    void updateSickDay_Failure() {
        SickDayDTO sickDayDTO = new SickDayDTO();

        when(sickDayService.updateSickDay(1L, sickDayDTO))
                .thenThrow(new RuntimeException("SickDay not found with id: 1"));

        ResponseEntity<?> response = sickDayController.updateSickDay(1L, sickDayDTO);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("SickDay not found with id: 1", response.getBody());
        verify(sickDayService, times(1)).updateSickDay(1L, sickDayDTO);
    }

    @Test
    void deleteSickDay_Success() {
        doNothing().when(sickDayService).deleteSickDay(1L);

        ResponseEntity<?> response = sickDayController.deleteSickDay(1L);

        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(sickDayService, times(1)).deleteSickDay(1L);
    }

    @Test
    void deleteSickDay_Failure() {
        doThrow(new RuntimeException("SickDay not found with id: 1"))
                .when(sickDayService).deleteSickDay(1L);

        ResponseEntity<?> response = sickDayController.deleteSickDay(1L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("SickDay not found with id: 1", response.getBody());
        verify(sickDayService, times(1)).deleteSickDay(1L);
    }
}
