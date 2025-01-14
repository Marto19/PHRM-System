package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.dto.DoctorAppointmentAllDTO;
import com.phrmSystem.phrmSystem.service.DoctorAppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class DoctorAppointmentController {

    private final DoctorAppointmentService doctorAppointmentService;

    public DoctorAppointmentController(DoctorAppointmentService doctorAppointmentService) {
        this.doctorAppointmentService = doctorAppointmentService;
    }

    @PostMapping
    public ResponseEntity<DoctorAppointmentAllDTO> createDoctorAppointment(@RequestBody DoctorAppointmentAllDTO appointmentDTO) {
        return ResponseEntity.ok(doctorAppointmentService.createDoctorAppointment(appointmentDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorAppointmentAllDTO> updateDoctorAppointment(
            @PathVariable Long id,
            @RequestBody DoctorAppointmentAllDTO doctorAppointmentAllDTO) {
        DoctorAppointmentAllDTO updatedAppointment = doctorAppointmentService.updateDoctorAppointment(id, doctorAppointmentAllDTO);
        return ResponseEntity.ok(updatedAppointment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorAppointmentAllDTO> getDoctorAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorAppointmentService.getDoctorAppointmentById(id));
    }

    @GetMapping
    public ResponseEntity<List<DoctorAppointmentAllDTO>> getAllDoctorAppointments() {
        return ResponseEntity.ok(doctorAppointmentService.getAllDoctorAppointments());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctorAppointment(@PathVariable Long id) {
        doctorAppointmentService.deleteDoctorAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
