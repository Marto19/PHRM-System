package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.dto.SickDayDTO;
import com.phrmSystem.phrmSystem.service.SickDayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sick-days")
public class SickDayController {

    private final SickDayService sickDayService;

    public SickDayController(SickDayService sickDayService) {
        this.sickDayService = sickDayService;
    }

    /**
     * Retrieves a SickDay by ID.
     *
     * @param id the ID of the SickDay to retrieve.
     * @return the SickDayDTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getSickDayById(@PathVariable Long id) {
        try {
            SickDayDTO sickDay = sickDayService.getSickDayById(id);
            return ResponseEntity.ok(sickDay);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Retrieves all SickDays.
     *
     * @return a list of SickDayDTOs.
     */
    @GetMapping
    public ResponseEntity<List<SickDayDTO>> getAllSickDays() {
        List<SickDayDTO> sickDays = sickDayService.getAllSickDays();
        return ResponseEntity.ok(sickDays);
    }

    /**
     * Creates a new SickDay.
     *
     * @param sickDayDTO the SickDayDTO containing SickDay details.
     * @return the created SickDayDTO.
     */
    @PostMapping
    public ResponseEntity<?> createSickDay(@RequestBody SickDayDTO sickDayDTO) {
        try {
            SickDayDTO createdSickDay = sickDayService.createSickDay(sickDayDTO);
            return ResponseEntity.status(201).body(createdSickDay);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Updates an existing SickDay.
     *
     * @param id         the ID of the SickDay to update.
     * @param sickDayDTO the new details for the SickDay.
     * @return the updated SickDayDTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSickDay(@PathVariable Long id, @RequestBody SickDayDTO sickDayDTO) {
        try {
            SickDayDTO updatedSickDay = sickDayService.updateSickDay(id, sickDayDTO);
            return ResponseEntity.ok(updatedSickDay);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Deletes a SickDay by ID.
     *
     * @param id the ID of the SickDay to delete.
     * @return a ResponseEntity with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSickDay(@PathVariable Long id) {
        try {
            sickDayService.deleteSickDay(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Retrieves the month with the most SickDays.
     *
     * @return a list of objects representing the month with the most SickDays.
     */
    @GetMapping("/most-sick-leaves-month")
    public ResponseEntity<?> getMonthWithMostSickLeaves() {
        try {
            // Replace with real repository method call if needed
            List<Object[]> result = sickDayService.getMonthWithMostSickLeaves();
            return ResponseEntity.ok(result);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Retrieves the doctors who issued the most SickDays.
     *
     * @return a list of objects representing the top doctors with the most SickDays.
     */
    @GetMapping("/top-doctors")
    public ResponseEntity<?> getDoctorsWithMostSickLeaves() {
        try {
            // Replace with real repository method call if needed
            List<Object[]> result = sickDayService.getDoctorsWithMostSickLeaves();
            return ResponseEntity.ok(result);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
