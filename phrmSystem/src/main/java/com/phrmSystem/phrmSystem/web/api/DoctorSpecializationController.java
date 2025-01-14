package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.dto.DoctorSpecializationDTO;
import com.phrmSystem.phrmSystem.service.DoctorSpecializationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor-specializations")
public class DoctorSpecializationController {

    private final DoctorSpecializationService doctorSpecializationService;

    public DoctorSpecializationController(DoctorSpecializationService doctorSpecializationService) {
        this.doctorSpecializationService = doctorSpecializationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorSpecializationDTO> getDoctorSpecializationById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorSpecializationService.getDoctorSpecializationById(id));
    }

    @GetMapping
    public ResponseEntity<List<DoctorSpecializationDTO>> getAllDoctorSpecializations() {
        return ResponseEntity.ok(doctorSpecializationService.getAllDoctorSpecializations());
    }

    @PostMapping
    public ResponseEntity<DoctorSpecializationDTO> createDoctorSpecialization(@RequestBody DoctorSpecializationDTO doctorSpecializationDTO) {
        return ResponseEntity.ok(doctorSpecializationService.createDoctorSpecialization(doctorSpecializationDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorSpecializationDTO> updateDoctorSpecialization(@PathVariable Long id, @RequestBody DoctorSpecializationDTO doctorSpecializationDTO) {
        return ResponseEntity.ok(doctorSpecializationService.updateDoctorSpecialization(id, doctorSpecializationDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctorSpecialization(@PathVariable Long id) {
        doctorSpecializationService.deleteDoctorSpecialization(id);
        return ResponseEntity.noContent().build();
    }
}
