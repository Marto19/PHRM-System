package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.dto.DiagnosisDTO;
import com.phrmSystem.phrmSystem.service.DiagnosisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diagnoses")
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    public DiagnosisController(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
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
}
