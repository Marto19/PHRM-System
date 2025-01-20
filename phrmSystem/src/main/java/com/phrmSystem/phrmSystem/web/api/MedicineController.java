package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.data.entity.Medicine;
import com.phrmSystem.phrmSystem.data.repo.MedicineRepository;
import com.phrmSystem.phrmSystem.dto.MedicineDTO;
import com.phrmSystem.phrmSystem.service.MedicineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineService medicineService;
    private final MedicineRepository medicineRepository;

    public MedicineController(MedicineService medicineService, MedicineRepository medicineRepository) {
        this.medicineService = medicineService;
        this.medicineRepository = medicineRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineDTO> getMedicineById(@PathVariable Long id) {
        return ResponseEntity.ok(medicineService.getMedicineById(id));
    }

    @GetMapping
    public ResponseEntity<List<MedicineDTO>> getAllMedicines() {
        return ResponseEntity.ok(medicineService.getAllMedicines());
    }

    @PostMapping
    public ResponseEntity<MedicineDTO> createMedicine(@RequestBody MedicineDTO medicineDTO) {
        return ResponseEntity.ok(medicineService.createMedicine(medicineDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineDTO> updateMedicine(@PathVariable Long id, @RequestBody MedicineDTO medicineDTO) {
        return ResponseEntity.ok(medicineService.updateMedicine(id, medicineDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return ResponseEntity.noContent().build();
    }

    // Get medicines for a specific diagnosis
    @GetMapping("/diagnosis/{diagnosisId}")
    public ResponseEntity<List<Medicine>> getMedicinesByDiagnosis(@PathVariable Long diagnosisId) {
        return ResponseEntity.ok(medicineRepository.findMedicinesByDiagnosisId(diagnosisId));
    }
}
