package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.data.repo.SickDayRepository;
import com.phrmSystem.phrmSystem.dto.SickDayDTO;
import com.phrmSystem.phrmSystem.service.SickDayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sick-days")
public class SickDayController {

    private final SickDayService sickDayService;
    private final SickDayRepository sickDayRepository;

    public SickDayController(SickDayService sickDayService, SickDayRepository sickDayRepository) {
        this.sickDayService = sickDayService;
        this.sickDayRepository = sickDayRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SickDayDTO> getSickDayById(@PathVariable Long id) {
        return ResponseEntity.ok(sickDayService.getSickDayById(id));
    }

    @GetMapping
    public ResponseEntity<List<SickDayDTO>> getAllSickDays() {
        return ResponseEntity.ok(sickDayService.getAllSickDays());
    }

    @PostMapping
    public ResponseEntity<SickDayDTO> createSickDay(@RequestBody SickDayDTO sickDayDTO) {
        return ResponseEntity.ok(sickDayService.createSickDay(sickDayDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SickDayDTO> updateSickDay(@PathVariable Long id, @RequestBody SickDayDTO sickDayDTO) {
        return ResponseEntity.ok(sickDayService.updateSickDay(id, sickDayDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSickDay(@PathVariable Long id) {
        sickDayService.deleteSickDay(id);
        return ResponseEntity.noContent().build();
    }

    // Get the month with the most sick leaves
    @GetMapping("/most-sick-leaves-month")
    public ResponseEntity<List<Object[]>> getMonthWithMostSickLeaves() {
        return ResponseEntity.ok(sickDayRepository.findMonthWithMostSickLeaves());
    }

    // Get the doctors who issued the most sick leaves
    @GetMapping("/top-doctors")
    public ResponseEntity<List<Object[]>> getDoctorsWithMostSickLeaves() {
        return ResponseEntity.ok(sickDayRepository.findDoctorsWithMostSickLeaves());
    }
}
