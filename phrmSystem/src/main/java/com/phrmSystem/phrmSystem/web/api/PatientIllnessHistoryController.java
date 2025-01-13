package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.dto.PatientIllnessHistoryDTO;
import com.phrmSystem.phrmSystem.service.PatientIllnessHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/illness-histories")
public class PatientIllnessHistoryController {

    private final PatientIllnessHistoryService service;

    public PatientIllnessHistoryController(PatientIllnessHistoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PatientIllnessHistoryDTO>> getAllIllnessHistories() {
        return ResponseEntity.ok(service.getAllIllnessHistories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientIllnessHistoryDTO> getIllnessHistoryById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getIllnessHistoryById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PatientIllnessHistoryDTO>> getIllnessHistoriesByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(service.getIllnessHistoriesByPatientId(patientId));
    }

    @PostMapping
    public ResponseEntity<PatientIllnessHistoryDTO> createIllnessHistory(@RequestBody PatientIllnessHistoryDTO dto) {
        return ResponseEntity.ok(service.createIllnessHistory(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientIllnessHistoryDTO> updateIllnessHistory(@PathVariable Long id, @RequestBody PatientIllnessHistoryDTO dto) {
        return ResponseEntity.ok(service.updateIllnessHistory(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIllnessHistory(@PathVariable Long id) {
        service.deleteIllnessHistory(id);
        return ResponseEntity.noContent().build();
    }
}
