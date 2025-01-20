package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.data.entity.Diagnosis;
import com.phrmSystem.phrmSystem.data.repo.DiagnosisRepository;
import com.phrmSystem.phrmSystem.dto.DiagnosisDTO;
import com.phrmSystem.phrmSystem.service.DiagnosisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diagnoses")
public class DiagnosisController {

    private final DiagnosisService diagnosisService;
    private final DiagnosisRepository diagnosisRepository;

    public DiagnosisController(DiagnosisService diagnosisService, DiagnosisRepository diagnosisRepository) {
        this.diagnosisService = diagnosisService;
        this.diagnosisRepository = diagnosisRepository;
    }

    @PostMapping
    public ResponseEntity<DiagnosisDTO> createDiagnosis(@RequestBody DiagnosisDTO diagnosisDTO) {
        return ResponseEntity.ok(diagnosisService.createDiagnosis(diagnosisDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiagnosisDTO> updateDiagnosis(@PathVariable Long id, @RequestBody DiagnosisDTO diagnosisDTO) {
        return ResponseEntity.ok(diagnosisService.updateDiagnosis(id, diagnosisDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiagnosis(@PathVariable Long id) {
        diagnosisService.deleteDiagnosis(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<DiagnosisDTO>> getAllDiagnoses() {
        return ResponseEntity.ok(diagnosisService.getAllDiagnoses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiagnosisDTO> getDiagnosisById(@PathVariable Long id) {
        return ResponseEntity.ok(diagnosisService.getDiagnosisById(id));
    }

    //TODO: TEST THESE BELOW

    // Get the most common diagnoses
    @GetMapping("/common")
    public ResponseEntity<List<Object[]>> getMostCommonDiagnoses() {
        return ResponseEntity.ok(diagnosisRepository.findMostCommonDiagnoses());
    }

    // Get diagnoses by name
    @GetMapping("/search")
    public ResponseEntity<List<Diagnosis>> getDiagnosesByName(@RequestParam String name) {
        return ResponseEntity.ok(diagnosisRepository.findByDiagnosisName(name));
    }
}
