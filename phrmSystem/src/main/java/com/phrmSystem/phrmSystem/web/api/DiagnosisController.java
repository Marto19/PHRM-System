package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.data.entity.Diagnosis;
import com.phrmSystem.phrmSystem.data.repo.DiagnosisRepository;
import com.phrmSystem.phrmSystem.dto.DiagnosisDTO;
import com.phrmSystem.phrmSystem.service.DiagnosisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing diagnoses.
 */
@RestController
@RequestMapping("/api/diagnoses")
public class DiagnosisController {

    private final DiagnosisService diagnosisService;
    private final DiagnosisRepository diagnosisRepository;

    public DiagnosisController(DiagnosisService diagnosisService, DiagnosisRepository diagnosisRepository) {
        this.diagnosisService = diagnosisService;
        this.diagnosisRepository = diagnosisRepository;
    }

    /**
     * Creates a new diagnosis.
     *
     * @param diagnosisDTO the diagnosis details.
     * @return the created diagnosis.
     */
    @PostMapping
    public ResponseEntity<?> createDiagnosis(@RequestBody DiagnosisDTO diagnosisDTO) {
        try {
            return ResponseEntity.ok(diagnosisService.createDiagnosis(diagnosisDTO));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Updates an existing diagnosis.
     *
     * @param id           the ID of the diagnosis to update.
     * @param diagnosisDTO the updated diagnosis details.
     * @return the updated diagnosis.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDiagnosis(@PathVariable Long id, @RequestBody DiagnosisDTO diagnosisDTO) {
        try {
            return ResponseEntity.ok(diagnosisService.updateDiagnosis(id, diagnosisDTO));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Deletes a diagnosis.
     *
     * @param id the ID of the diagnosis to delete.
     * @return a response indicating the deletion status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiagnosis(@PathVariable Long id) {
        try {
            diagnosisService.deleteDiagnosis(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Retrieves all diagnoses.
     *
     * @return a list of all diagnoses.
     */
    @GetMapping
    public ResponseEntity<List<DiagnosisDTO>> getAllDiagnoses() {
        return ResponseEntity.ok(diagnosisService.getAllDiagnoses());
    }

    /**
     * Retrieves a specific diagnosis by its ID.
     *
     * @param id the ID of the diagnosis.
     * @return the diagnosis details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getDiagnosisById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(diagnosisService.getDiagnosisById(id));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Retrieves the most common diagnoses.
     *
     * @return a list of the most common diagnoses.
     */
    @GetMapping("/common")
    public ResponseEntity<?> getMostCommonDiagnoses() {
        try {
            return ResponseEntity.ok(diagnosisRepository.findMostCommonDiagnoses());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Failed to retrieve most common diagnoses.");
        }
    }

    /**
     * Searches diagnoses by name.
     *
     * @param name the name to search for.
     * @return a list of diagnoses matching the name.
     */
    @GetMapping("/search")
    public ResponseEntity<?> getDiagnosesByName(@RequestParam String name) {
        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body("Diagnosis name cannot be null or empty.");
        }

        List<Diagnosis> diagnoses = diagnosisRepository.findByDiagnosisName(name);
        if (diagnoses.isEmpty()) {
            return ResponseEntity.ok("No diagnoses found matching the name: " + name);
        }

        return ResponseEntity.ok(diagnoses);
    }
}
