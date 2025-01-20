package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.dto.DoctorSpecializationDTO;
import com.phrmSystem.phrmSystem.service.DoctorSpecializationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing doctor specializations.
 */
@RestController
@RequestMapping("/api/doctor-specializations")
public class DoctorSpecializationController {

    private final DoctorSpecializationService doctorSpecializationService;

    public DoctorSpecializationController(DoctorSpecializationService doctorSpecializationService) {
        this.doctorSpecializationService = doctorSpecializationService;
    }

    /**
     * Retrieves a doctor specialization by ID.
     *
     * @param id the ID of the specialization.
     * @return the DoctorSpecializationDTO representation.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorSpecializationById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(doctorSpecializationService.getDoctorSpecializationById(id));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Retrieves all doctor specializations.
     *
     * @return a list of DoctorSpecializationDTOs.
     */
    @GetMapping
    public ResponseEntity<List<DoctorSpecializationDTO>> getAllDoctorSpecializations() {
        return ResponseEntity.ok(doctorSpecializationService.getAllDoctorSpecializations());
    }

    /**
     * Creates a new doctor specialization.
     *
     * @param doctorSpecializationDTO the details of the new specialization.
     * @return the created DoctorSpecializationDTO.
     */
    @PostMapping
    public ResponseEntity<?> createDoctorSpecialization(@RequestBody DoctorSpecializationDTO doctorSpecializationDTO) {
        try {
            return ResponseEntity.status(201).body(doctorSpecializationService.createDoctorSpecialization(doctorSpecializationDTO));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Updates an existing doctor specialization.
     *
     * @param id                     the ID of the specialization to update.
     * @param doctorSpecializationDTO the updated specialization details.
     * @return the updated DoctorSpecializationDTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctorSpecialization(@PathVariable Long id, @RequestBody DoctorSpecializationDTO doctorSpecializationDTO) {
        try {
            return ResponseEntity.ok(doctorSpecializationService.updateDoctorSpecialization(id, doctorSpecializationDTO));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Deletes a doctor specialization by ID.
     *
     * @param id the ID of the specialization to delete.
     * @return a 204 No Content response if successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctorSpecialization(@PathVariable Long id) {
        try {
            doctorSpecializationService.deleteDoctorSpecialization(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
