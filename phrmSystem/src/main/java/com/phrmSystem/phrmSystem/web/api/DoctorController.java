package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.entity.DoctorSpecialization;
import com.phrmSystem.phrmSystem.data.entity.DoctorAppointment;
import com.phrmSystem.phrmSystem.dto.UserDTO;
import com.phrmSystem.phrmSystem.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<User> createDoctor(@RequestBody User doctor) {
        User createdDoctor = doctorService.createDoctor(doctor);
        return ResponseEntity.ok(createdDoctor);
    }

    // Endpoint to update an existing doctor
    @PutMapping("/{id}")
    public ResponseEntity<User> updateDoctor(@PathVariable Long id, @RequestBody User updatedDoctor) {
        User updatedDoctorResponse = doctorService.updateDoctor(id, updatedDoctor);
        return ResponseEntity.ok(updatedDoctorResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    // Fetch all doctors
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllDoctors() {
        List<UserDTO> doctorDTOs = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctorDTOs);
    }


    // Fetch a doctor by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    // Fetch a doctor by unique ID
    @GetMapping("/unique/{uniqueId}")
    public ResponseEntity<User> getDoctorByUniqueId(@PathVariable String uniqueId) {
        return ResponseEntity.ok(doctorService.getDoctorByUniqueId(uniqueId));
    }

    // Fetch all personal doctors
    @GetMapping("/personal")
    public ResponseEntity<List<User>> getAllPersonalDoctors() {
        return ResponseEntity.ok(doctorService.getAllPersonalDoctors());
    }

    // Fetch a doctor's specializations
    @GetMapping("/{id}/specializations")
    public ResponseEntity<List<DoctorSpecialization>> getDoctorSpecializations(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorSpecializations(id));
    }

    // Add a specialization to a doctor
    @PostMapping("/{id}/specializations")
    public ResponseEntity<User> addSpecializationToDoctor(
            @PathVariable Long id,
            @RequestBody DoctorSpecialization specialization) {
        return ResponseEntity.ok(doctorService.addSpecializationToDoctor(id, specialization));
    }

    // Fetch a doctor's appointments
    @GetMapping("/{id}/appointments")
    public ResponseEntity<List<DoctorAppointment>> getDoctorAppointments(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorAppointments(id));
    }
}
