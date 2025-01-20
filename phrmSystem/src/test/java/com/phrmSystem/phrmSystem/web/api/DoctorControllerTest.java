package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.dto.AppointmentDTO;
import com.phrmSystem.phrmSystem.dto.SpecializationDTO;
import com.phrmSystem.phrmSystem.dto.UserDTO;
import com.phrmSystem.phrmSystem.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class DoctorControllerTest {

    @Mock
    private DoctorService doctorService;

    private DoctorController doctorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doctorController = new DoctorController(doctorService);
    }

    @Test
    void createDoctor_Success() {
        User doctor = new User();
        doctor.setId(1L);

        when(doctorService.createDoctor(doctor)).thenReturn(doctor);

        ResponseEntity<?> response = doctorController.createDoctor(doctor);

        assertEquals(CREATED, response.getStatusCode());
        assertEquals(doctor, response.getBody());
        verify(doctorService, times(1)).createDoctor(doctor);
    }

    @Test
    void createDoctor_Failure() {
        User doctor = new User();

        when(doctorService.createDoctor(doctor))
                .thenThrow(new RuntimeException("Validation error"));

        ResponseEntity<?> response = doctorController.createDoctor(doctor);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation error", response.getBody());
        verify(doctorService, times(1)).createDoctor(doctor);
    }

    @Test
    void updateDoctor_Success() {
        User doctor = new User();
        doctor.setId(1L);

        when(doctorService.updateDoctor(1L, doctor)).thenReturn(doctor);

        ResponseEntity<?> response = doctorController.updateDoctor(1L, doctor);

        assertEquals(OK, response.getStatusCode());
        assertEquals(doctor, response.getBody());
        verify(doctorService, times(1)).updateDoctor(1L, doctor);
    }

    @Test
    void updateDoctor_Failure() {
        User doctor = new User();

        when(doctorService.updateDoctor(1L, doctor))
                .thenThrow(new RuntimeException("Doctor not found"));

        ResponseEntity<?> response = doctorController.updateDoctor(1L, doctor);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Doctor not found", response.getBody());
        verify(doctorService, times(1)).updateDoctor(1L, doctor);
    }

    @Test
    void deleteDoctor_Success() {
        doNothing().when(doctorService).deleteDoctor(1L);

        ResponseEntity<?> response = doctorController.deleteDoctor(1L);

        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(doctorService, times(1)).deleteDoctor(1L);
    }

    @Test
    void deleteDoctor_Failure() {
        doThrow(new RuntimeException("Cannot delete doctor"))
                .when(doctorService).deleteDoctor(1L);

        ResponseEntity<?> response = doctorController.deleteDoctor(1L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Cannot delete doctor", response.getBody());
        verify(doctorService, times(1)).deleteDoctor(1L);
    }

    @Test
    void getAllDoctors_Success() {
        UserDTO doctorDTO = new UserDTO();
        doctorDTO.setId(1L);

        when(doctorService.getAllDoctors()).thenReturn(List.of(doctorDTO));

        ResponseEntity<List<UserDTO>> response = doctorController.getAllDoctors();

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(doctorService, times(1)).getAllDoctors();
    }

    @Test
    void getDoctorById_Success() {
        UserDTO doctorDTO = new UserDTO();
        doctorDTO.setId(1L);

        when(doctorService.getDoctorById(1L)).thenReturn(doctorDTO);

        ResponseEntity<?> response = doctorController.getDoctorById(1L);

        assertEquals(OK, response.getStatusCode());
        assertEquals(doctorDTO, response.getBody());
        verify(doctorService, times(1)).getDoctorById(1L);
    }

    @Test
    void getDoctorById_Failure() {
        when(doctorService.getDoctorById(1L))
                .thenThrow(new RuntimeException("Doctor not found"));

        ResponseEntity<?> response = doctorController.getDoctorById(1L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Doctor not found", response.getBody());
        verify(doctorService, times(1)).getDoctorById(1L);
    }

    @Test
    void getDoctorByUniqueId_Success() {
        User doctor = new User();
        doctor.setId(1L);

        when(doctorService.getDoctorByUniqueId("D123")).thenReturn(doctor);

        ResponseEntity<?> response = doctorController.getDoctorByUniqueId("D123");

        assertEquals(OK, response.getStatusCode());
        assertEquals(doctor, response.getBody());
        verify(doctorService, times(1)).getDoctorByUniqueId("D123");
    }

    @Test
    void getDoctorByUniqueId_Failure() {
        when(doctorService.getDoctorByUniqueId("D123"))
                .thenThrow(new RuntimeException("Doctor not found"));

        ResponseEntity<?> response = doctorController.getDoctorByUniqueId("D123");

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Doctor not found", response.getBody());
        verify(doctorService, times(1)).getDoctorByUniqueId("D123");
    }

    @Test
    void getDoctorSpecializations_Success() {
        SpecializationDTO specializationDTO = new SpecializationDTO();
        specializationDTO.setName("Cardiology");

        when(doctorService.getDoctorSpecializations(1L)).thenReturn(List.of(specializationDTO));

        ResponseEntity<?> response = doctorController.getDoctorSpecializations(1L);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
        verify(doctorService, times(1)).getDoctorSpecializations(1L);
    }


    @Test
    void addSpecializationToDoctor_Success() {
        UserDTO doctorDTO = new UserDTO();
        doctorDTO.setId(1L);

        SpecializationDTO specializationDTO = new SpecializationDTO();
        specializationDTO.setName("Cardiology");

        when(doctorService.addSpecializationToDoctor(1L, specializationDTO)).thenReturn(doctorDTO);

        ResponseEntity<?> response = doctorController.addSpecializationToDoctor(1L, specializationDTO);

        assertEquals(OK, response.getStatusCode());
        assertEquals(doctorDTO, response.getBody());
        verify(doctorService, times(1)).addSpecializationToDoctor(1L, specializationDTO);
    }


    @Test
    void getDoctorAppointments_Success() {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(1L);

        when(doctorService.getDoctorAppointments(1L)).thenReturn(List.of(appointmentDTO));

        ResponseEntity<?> response = doctorController.getDoctorAppointments(1L);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
        verify(doctorService, times(1)).getDoctorAppointments(1L);
    }

    @Test
    void getAllPersonalDoctors_Success() {
        User doctor = new User();
        doctor.setId(1L);

        when(doctorService.getAllPersonalDoctors()).thenReturn(List.of(doctor));

        ResponseEntity<?> response = doctorController.getAllPersonalDoctors();

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
        verify(doctorService, times(1)).getAllPersonalDoctors();
    }
}
