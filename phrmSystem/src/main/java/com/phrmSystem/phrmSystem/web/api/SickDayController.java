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
}
