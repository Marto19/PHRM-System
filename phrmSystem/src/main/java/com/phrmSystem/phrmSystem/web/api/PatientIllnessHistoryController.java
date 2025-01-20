package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.dto.PatientIllnessHistoryDTO;
import com.phrmSystem.phrmSystem.service.PatientIllnessHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Patient Illness Histories.
 */
@RestController
@RequestMapping("/api/illness-histories")
public class PatientIllnessHistoryController {

    private final PatientIllnessHistoryService service;

    public PatientIllnessHistoryController(PatientIllnessHistoryService service) {
        this.service = service;
    }

    /**
     * Retrieves all patient illness histories.
     *
     * @return a list of PatientIllnessHistoryDTO objects.
     */
    @GetMapping
    public ResponseEntity<List<PatientIllnessHistoryDTO>> getAllIllnessHistories() {
        List<PatientIllnessHistoryDTO> histories = service.getAllIllnessHistories();
        return ResponseEntity.ok(histories);
    }

    /**
     * Retrieves a specific illness history by its ID.
     *
     * @param id the ID of the illness history.
     * @return the PatientIllnessHistoryDTO object.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getIllnessHistoryById(@PathVariable Long id) {
        try {
            PatientIllnessHistoryDTO history = service.getIllnessHistoryById(id);
            return ResponseEntity.ok(history);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Retrieves illness histories by patient ID.
     *
     * @param patientId the patient ID.
     * @return a list of PatientIllnessHistoryDTO objects.
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getIllnessHistoriesByPatientId(@PathVariable Long patientId) {
        try {
            List<PatientIllnessHistoryDTO> histories = service.getIllnessHistoriesByPatientId(patientId);
            return ResponseEntity.ok(histories);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Creates a new illness history.
     *
     * @param dto the PatientIllnessHistoryDTO containing the details.
     * @return the created PatientIllnessHistoryDTO.
     */
    @PostMapping
    public ResponseEntity<?> createIllnessHistory(@RequestBody PatientIllnessHistoryDTO dto) {
        try {
            PatientIllnessHistoryDTO created = service.createIllnessHistory(dto);
            return ResponseEntity.status(201).body(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Updates an existing illness history.
     *
     * @param id  the ID of the illness history.
     * @param dto the updated PatientIllnessHistoryDTO.
     * @return the updated PatientIllnessHistoryDTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateIllnessHistory(@PathVariable Long id, @RequestBody PatientIllnessHistoryDTO dto) {
        try {
            PatientIllnessHistoryDTO updated = service.updateIllnessHistory(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Deletes an illness history by its ID.
     *
     * @param id the ID of the illness history to delete.
     * @return an HTTP 204 (No Content) status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIllnessHistory(@PathVariable Long id) {
        try {
            service.deleteIllnessHistory(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
