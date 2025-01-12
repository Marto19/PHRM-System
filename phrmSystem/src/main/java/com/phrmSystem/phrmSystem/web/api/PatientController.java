package com.phrmSystem.phrmSystem.controller;

import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.entity.PatientIllnessHistory;
import com.phrmSystem.phrmSystem.data.entity.DoctorAppointment;
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

    // Endpoint to create a new patient
    @PostMapping
    public ResponseEntity<User> createPatient(@RequestBody User patient) {
        return ResponseEntity.ok(patientService.createPatient(patient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updatePatient(@PathVariable Long id, @RequestBody User updatedPatient) {
        return ResponseEntity.ok(patientService.updatePatient(id, updatedPatient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    // Fetch all patients
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    // Fetch a patient by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    // Fetch a patient by unique identification
    @GetMapping("/unique/{uniqueIdentification}")
    public ResponseEntity<User> getPatientByUniqueIdentification(@PathVariable String uniqueIdentification) {
        return ResponseEntity.ok(patientService.getPatientByUniqueIdentification(uniqueIdentification));
    }

    // Fetch all patients with insurance paid
    @GetMapping("/insured")
    public ResponseEntity<List<User>> getPatientsWithInsurancePaid() {
        return ResponseEntity.ok(patientService.getPatientsWithInsurancePaid());
    }

    // Fetch a patient's illness history
    @GetMapping("/{id}/history")
    public ResponseEntity<List<PatientIllnessHistory>> getPatientIllnessHistory(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientIllnessHistory(id));
    }

    // Create an appointment for a patient
    @PostMapping("/{id}/appointments")
    public ResponseEntity<DoctorAppointment> createAppointment(
            @PathVariable Long id,
            @RequestBody DoctorAppointment appointment) {
        return ResponseEntity.ok(patientService.createAppointment(id, appointment));
    }
}
