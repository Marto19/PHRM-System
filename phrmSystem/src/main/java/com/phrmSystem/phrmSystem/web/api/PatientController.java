package com.phrmSystem.phrmSystem.controller;

import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.dto.DoctorAppointmentDTO;
import com.phrmSystem.phrmSystem.dto.PatientIllnessHistoryDTO;
import com.phrmSystem.phrmSystem.dto.UserDTO;
import com.phrmSystem.phrmSystem.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Creates a new patient.
     *
     * @param patient The User entity representing the patient.
     * @return The created patient entity.
     */
    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody User patient) {
        try {
            User createdPatient = patientService.createPatient(patient);
            return ResponseEntity.status(201).body(createdPatient);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Updates an existing patient.
     *
     * @param id             The ID of the patient to update.
     * @param updatedPatient The updated patient entity.
     * @return The updated patient entity.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Long id, @RequestBody User updatedPatient) {
        try {
            User updated = patientService.updatePatient(id, updatedPatient);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Deletes a patient by ID.
     *
     * @param id The ID of the patient to delete.
     * @return 204 No Content if successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {     //todo: not as gracefull as I want it to be
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Fetches all patients.
     *
     * @return A list of all patients as DTOs.
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    /**
     * Fetches a patient by ID.
     *
     * @param id The ID of the patient to fetch.
     * @return The patient DTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Long id) {
        try {
            UserDTO patientDTO = patientService.getPatientById(id);
            return ResponseEntity.ok(patientDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Fetches a patient by their unique identification.
     *
     * @param uniqueIdentification The unique identification of the patient.
     * @return The patient entity.
     */
    @GetMapping("/unique/{uniqueIdentification}")
    public ResponseEntity<?> getPatientByUniqueIdentification(@PathVariable String uniqueIdentification) {
        try {
            User patient = patientService.getPatientByUniqueIdentification(uniqueIdentification);
            return ResponseEntity.ok(patient);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Fetches all patients with insurance paid.
     *
     * @return A list of all patients with insurance paid.
     */
    @GetMapping("/insured")
    public ResponseEntity<List<User>> getPatientsWithInsurancePaid() {
        return ResponseEntity.ok(patientService.getPatientsWithInsurancePaid());
    }

    /**
     * Fetches the illness history of a patient.
     *
     * @param id The ID of the patient.
     * @return A list of illness history DTOs for the patient.
     */
    @GetMapping("/{id}/history")
    public ResponseEntity<?> getPatientIllnessHistory(@PathVariable Long id) {
        try {
            List<PatientIllnessHistoryDTO> history = patientService.getPatientIllnessHistory(id);
            return ResponseEntity.ok(history);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Creates an appointment for a patient.
     *
     * @param id            The ID of the patient.
     * @param appointmentDTO The details of the appointment to create.
     * @return The created DoctorAppointmentDTO.
     */
    @PostMapping("/{id}/appointments")
    public ResponseEntity<?> createAppointment(@PathVariable Long id, @RequestBody DoctorAppointmentDTO appointmentDTO) {
        try {
            DoctorAppointmentDTO createdAppointment = patientService.createAppointment(id, appointmentDTO);
            return ResponseEntity.status(201).body(createdAppointment);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
