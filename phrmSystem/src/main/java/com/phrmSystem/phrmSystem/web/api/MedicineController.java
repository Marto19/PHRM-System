package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.data.entity.Medicine;
import com.phrmSystem.phrmSystem.dto.MedicineDTO;
import com.phrmSystem.phrmSystem.service.MedicineService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing medicines.
 */
@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    /**
     * Retrieves a medicine by its ID.
     *
     * @param id the ID of the medicine.
     * @return the MedicineDTO representation.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getMedicineById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(medicineService.getMedicineById(id));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Retrieves all medicines.
     *
     * @return a list of MedicineDTOs.
     */
    @GetMapping
    public ResponseEntity<List<MedicineDTO>> getAllMedicines() {
        return ResponseEntity.ok(medicineService.getAllMedicines());
    }

    /**
     * Creates a new medicine.
     *
     * @param medicineDTO the details of the medicine to create.
     * @return the created MedicineDTO.
     */
    @PostMapping
    public ResponseEntity<?> createMedicine(@Valid @RequestBody MedicineDTO medicineDTO) {
        try {
            return ResponseEntity.status(201).body(medicineService.createMedicine(medicineDTO));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Updates an existing medicine by ID.
     *
     * @param id          the ID of the medicine to update.
     * @param medicineDTO the updated details.
     * @return the updated MedicineDTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMedicine(@PathVariable Long id, @Valid @RequestBody MedicineDTO medicineDTO) {
        try {
            return ResponseEntity.ok(medicineService.updateMedicine(id, medicineDTO));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Deletes a medicine by its ID.
     *
     * @param id the ID of the medicine to delete.
     * @return a no-content response if successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedicine(@PathVariable Long id) {
        try {
            medicineService.deleteMedicine(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Retrieves all medicines associated with a specific diagnosis.
     *
     * @param diagnosisId the ID of the diagnosis.
     * @return a list of medicines.
     */
    @GetMapping("/diagnosis/{diagnosisId}")
    public ResponseEntity<?> getMedicinesByDiagnosis(@PathVariable Long diagnosisId) {
        try {
            List<MedicineDTO> medicines = medicineService.getMedicinesByDiagnosis(diagnosisId);
            return ResponseEntity.ok(medicines);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
