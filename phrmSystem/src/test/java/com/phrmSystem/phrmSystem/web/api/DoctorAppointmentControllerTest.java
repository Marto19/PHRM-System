package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.data.entity.DoctorAppointment;
import com.phrmSystem.phrmSystem.data.repo.DoctorAppointmentRepository;
import com.phrmSystem.phrmSystem.dto.DoctorAppointmentAllDTO;
import com.phrmSystem.phrmSystem.service.DoctorAppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class DoctorAppointmentControllerTest {

    @Mock
    private DoctorAppointmentService doctorAppointmentService;

    @Mock
    private DoctorAppointmentRepository doctorAppointmentRepository;

    private DoctorAppointmentController doctorAppointmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doctorAppointmentController = new DoctorAppointmentController(doctorAppointmentService, doctorAppointmentRepository);
    }

    @Test
    void createDoctorAppointment_Success() {
        DoctorAppointmentAllDTO appointmentDTO = new DoctorAppointmentAllDTO();
        appointmentDTO.setId(1L);

        when(doctorAppointmentService.createDoctorAppointment(appointmentDTO)).thenReturn(appointmentDTO);

        ResponseEntity<?> response = doctorAppointmentController.createDoctorAppointment(appointmentDTO);

        assertEquals(OK, response.getStatusCode());
        assertEquals(appointmentDTO, response.getBody());
        verify(doctorAppointmentService, times(1)).createDoctorAppointment(appointmentDTO);
    }

    @Test
    void createDoctorAppointment_Failure() {
        DoctorAppointmentAllDTO appointmentDTO = new DoctorAppointmentAllDTO();

        when(doctorAppointmentService.createDoctorAppointment(appointmentDTO))
                .thenThrow(new RuntimeException("Validation failed."));

        ResponseEntity<?> response = doctorAppointmentController.createDoctorAppointment(appointmentDTO);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed.", response.getBody());
        verify(doctorAppointmentService, times(1)).createDoctorAppointment(appointmentDTO);
    }

    @Test
    void updateDoctorAppointment_Success() {
        DoctorAppointmentAllDTO appointmentDTO = new DoctorAppointmentAllDTO();
        appointmentDTO.setId(1L);

        when(doctorAppointmentService.updateDoctorAppointment(1L, appointmentDTO)).thenReturn(appointmentDTO);

        ResponseEntity<?> response = doctorAppointmentController.updateDoctorAppointment(1L, appointmentDTO);

        assertEquals(OK, response.getStatusCode());
        assertEquals(appointmentDTO, response.getBody());
        verify(doctorAppointmentService, times(1)).updateDoctorAppointment(1L, appointmentDTO);
    }

    @Test
    void updateDoctorAppointment_Failure() {
        DoctorAppointmentAllDTO appointmentDTO = new DoctorAppointmentAllDTO();

        when(doctorAppointmentService.updateDoctorAppointment(1L, appointmentDTO))
                .thenThrow(new RuntimeException("Appointment not found."));

        ResponseEntity<?> response = doctorAppointmentController.updateDoctorAppointment(1L, appointmentDTO);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Appointment not found.", response.getBody());
        verify(doctorAppointmentService, times(1)).updateDoctorAppointment(1L, appointmentDTO);
    }

    @Test
    void getDoctorAppointmentById_Success() {
        DoctorAppointmentAllDTO appointmentDTO = new DoctorAppointmentAllDTO();
        appointmentDTO.setId(1L);

        when(doctorAppointmentService.getDoctorAppointmentById(1L)).thenReturn(appointmentDTO);

        ResponseEntity<?> response = doctorAppointmentController.getDoctorAppointmentById(1L);

        assertEquals(OK, response.getStatusCode());
        assertEquals(appointmentDTO, response.getBody());
        verify(doctorAppointmentService, times(1)).getDoctorAppointmentById(1L);
    }

    @Test
    void getDoctorAppointmentById_Failure() {
        when(doctorAppointmentService.getDoctorAppointmentById(1L))
                .thenThrow(new RuntimeException("Appointment not found."));

        ResponseEntity<?> response = doctorAppointmentController.getDoctorAppointmentById(1L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Appointment not found.", response.getBody());
        verify(doctorAppointmentService, times(1)).getDoctorAppointmentById(1L);
    }

    @Test
    void getAllDoctorAppointments_Success() {
        DoctorAppointmentAllDTO appointmentDTO = new DoctorAppointmentAllDTO();
        appointmentDTO.setId(1L);

        when(doctorAppointmentService.getAllDoctorAppointments()).thenReturn(List.of(appointmentDTO));

        ResponseEntity<List<DoctorAppointmentAllDTO>> response = doctorAppointmentController.getAllDoctorAppointments();

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(doctorAppointmentService, times(1)).getAllDoctorAppointments();
    }

    @Test
    void deleteDoctorAppointment_Success() {
        doNothing().when(doctorAppointmentService).deleteDoctorAppointment(1L);

        ResponseEntity<?> response = doctorAppointmentController.deleteDoctorAppointment(1L);

        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(doctorAppointmentService, times(1)).deleteDoctorAppointment(1L);
    }

    @Test
    void deleteDoctorAppointment_Failure() {
        doThrow(new RuntimeException("Appointment not found."))
                .when(doctorAppointmentService).deleteDoctorAppointment(1L);

        ResponseEntity<?> response = doctorAppointmentController.deleteDoctorAppointment(1L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Appointment not found.", response.getBody());
        verify(doctorAppointmentService, times(1)).deleteDoctorAppointment(1L);
    }

    @Test
    void getAppointmentsByDoctorAndDateRange_Success() {
        DoctorAppointment appointment = new DoctorAppointment();
        appointment.setId(1L);

        when(doctorAppointmentRepository.findAppointmentsByDoctorIdAndDateRange(
                eq(1L),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of(appointment));

        ResponseEntity<?> response = doctorAppointmentController.getAppointmentsByDoctorAndDateRange(
                1L, "2025-01-01T00:00", "2025-01-10T23:59");

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
        verify(doctorAppointmentRepository, times(1))
                .findAppointmentsByDoctorIdAndDateRange(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void getAppointmentsByDoctorAndDateRange_Failure() {
        ResponseEntity<?> response = doctorAppointmentController.getAppointmentsByDoctorAndDateRange(
                1L, "invalid-date", "invalid-date");

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid date format or other error: Invalid date range.", response.getBody());
    }

}
