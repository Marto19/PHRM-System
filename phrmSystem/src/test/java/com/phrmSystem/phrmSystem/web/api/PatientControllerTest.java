package com.phrmSystem.phrmSystem.controller;

import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.dto.DoctorAppointmentDTO;
import com.phrmSystem.phrmSystem.dto.PatientIllnessHistoryDTO;
import com.phrmSystem.phrmSystem.dto.UserDTO;
import com.phrmSystem.phrmSystem.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class PatientControllerTest {

    @Mock
    private PatientService patientService;

    private com.phrmSystem.phrmSystem.controller.PatientController patientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        patientController = new com.phrmSystem.phrmSystem.controller.PatientController(patientService);
    }

    @Test
    void createPatient_Success() {
        User patient = new User();
        patient.setFirstName("John");

        when(patientService.createPatient(patient)).thenReturn(patient);

        ResponseEntity<?> response = patientController.createPatient(patient);

        assertEquals(CREATED, response.getStatusCode());
        assertEquals(patient, response.getBody());
        verify(patientService, times(1)).createPatient(patient);
    }

    @Test
    void createPatient_Failure() {
        User patient = new User();
        when(patientService.createPatient(patient)).thenThrow(new RuntimeException("Validation failed"));

        ResponseEntity<?> response = patientController.createPatient(patient);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed", response.getBody());
        verify(patientService, times(1)).createPatient(patient);
    }

    @Test
    void updatePatient_Success() {
        User updatedPatient = new User();
        updatedPatient.setFirstName("Jane");

        when(patientService.updatePatient(1L, updatedPatient)).thenReturn(updatedPatient);

        ResponseEntity<?> response = patientController.updatePatient(1L, updatedPatient);

        assertEquals(OK, response.getStatusCode());
        assertEquals(updatedPatient, response.getBody());
        verify(patientService, times(1)).updatePatient(1L, updatedPatient);
    }

    @Test
    void updatePatient_Failure() {
        User updatedPatient = new User();
        when(patientService.updatePatient(1L, updatedPatient)).thenThrow(new RuntimeException("Patient not found"));

        ResponseEntity<?> response = patientController.updatePatient(1L, updatedPatient);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Patient not found", response.getBody());
        verify(patientService, times(1)).updatePatient(1L, updatedPatient);
    }

    @Test
    void deletePatient_Success() {
        doNothing().when(patientService).deletePatient(1L);

        ResponseEntity<?> response = patientController.deletePatient(1L);

        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(patientService, times(1)).deletePatient(1L);
    }

    @Test
    void deletePatient_Failure() {
        doThrow(new RuntimeException("Patient not found")).when(patientService).deletePatient(1L);

        ResponseEntity<?> response = patientController.deletePatient(1L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Patient not found", response.getBody());
        verify(patientService, times(1)).deletePatient(1L);
    }

    @Test
    void getAllPatients_Success() {
        UserDTO patient = new UserDTO();
        patient.setFirstName("John");

        when(patientService.getAllPatients()).thenReturn(List.of(patient));

        ResponseEntity<List<UserDTO>> response = patientController.getAllPatients();

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(patientService, times(1)).getAllPatients();
    }

    @Test
    void getPatientById_Success() {
        UserDTO patient = new UserDTO();
        patient.setId(1L);

        when(patientService.getPatientById(1L)).thenReturn(patient);

        ResponseEntity<?> response = patientController.getPatientById(1L);

        assertEquals(OK, response.getStatusCode());
        assertEquals(patient, response.getBody());
        verify(patientService, times(1)).getPatientById(1L);
    }

    @Test
    void getPatientById_Failure() {
        when(patientService.getPatientById(1L)).thenThrow(new RuntimeException("Patient not found"));

        ResponseEntity<?> response = patientController.getPatientById(1L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Patient not found", response.getBody());
        verify(patientService, times(1)).getPatientById(1L);
    }

    @Test
    void getPatientByUniqueIdentification_Success() {
        User patient = new User();
        patient.setUniqueIdentification("12345");

        when(patientService.getPatientByUniqueIdentification("12345")).thenReturn(patient);

        ResponseEntity<?> response = patientController.getPatientByUniqueIdentification("12345");

        assertEquals(OK, response.getStatusCode());
        assertEquals(patient, response.getBody());
        verify(patientService, times(1)).getPatientByUniqueIdentification("12345");
    }

    @Test
    void getPatientByUniqueIdentification_Failure() {
        when(patientService.getPatientByUniqueIdentification("12345"))
                .thenThrow(new RuntimeException("Patient not found"));

        ResponseEntity<?> response = patientController.getPatientByUniqueIdentification("12345");

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Patient not found", response.getBody());
        verify(patientService, times(1)).getPatientByUniqueIdentification("12345");
    }

    @Test
    void getPatientsWithInsurancePaid_Success() {
        User patient = new User();
        patient.setInsurancePaidLast6Months(true);

        when(patientService.getPatientsWithInsurancePaid()).thenReturn(List.of(patient));

        ResponseEntity<List<User>> response = patientController.getPatientsWithInsurancePaid();

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(patientService, times(1)).getPatientsWithInsurancePaid();
    }

    @Test
    void getPatientIllnessHistory_Success() {
        PatientIllnessHistoryDTO history = new PatientIllnessHistoryDTO();
        history.setIllnessName("Flu");

        when(patientService.getPatientIllnessHistory(1L)).thenReturn(List.of(history));

        ResponseEntity<?> response = patientController.getPatientIllnessHistory(1L);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(patientService, times(1)).getPatientIllnessHistory(1L);
    }

    @Test
    void createAppointment_Success() {
        DoctorAppointmentDTO appointment = new DoctorAppointmentDTO();
        appointment.setId(1L);

        when(patientService.createAppointment(1L, appointment)).thenReturn(appointment);

        ResponseEntity<?> response = patientController.createAppointment(1L, appointment);

        assertEquals(CREATED, response.getStatusCode());
        assertEquals(appointment, response.getBody());
        verify(patientService, times(1)).createAppointment(1L, appointment);
    }

    @Test
    void createAppointment_Failure() {
        DoctorAppointmentDTO appointment = new DoctorAppointmentDTO();

        when(patientService.createAppointment(1L, appointment))
                .thenThrow(new RuntimeException("Appointment creation failed"));

        ResponseEntity<?> response = patientController.createAppointment(1L, appointment);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Appointment creation failed", response.getBody());
        verify(patientService, times(1)).createAppointment(1L, appointment);
    }
}
