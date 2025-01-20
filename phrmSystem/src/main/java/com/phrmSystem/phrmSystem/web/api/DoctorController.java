package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.entity.DoctorSpecialization;
import com.phrmSystem.phrmSystem.data.entity.DoctorAppointment;
import com.phrmSystem.phrmSystem.data.repo.DoctorRepository;
import com.phrmSystem.phrmSystem.dto.AppointmentDTO;
import com.phrmSystem.phrmSystem.dto.SpecializationDTO;
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

    /**
     * Creates a new doctor.
     *
     * @param doctor the doctor entity to be created.
     * @return the created doctor with HTTP status 201 (Created) or an error message with HTTP status 400 (Bad Request).
     */
    @PostMapping
    public ResponseEntity<?> createDoctor(@RequestBody User doctor) {
        try {
            User createdDoctor = doctorService.createDoctor(doctor);
            return ResponseEntity.status(201).body(createdDoctor);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Updates an existing doctor by ID.
     *
     * @param id the ID of the doctor to update.
     * @param updatedDoctor the updated doctor details.
     * @return the updated doctor with HTTP status 200 (OK) or an error message with HTTP status 400 (Bad Request).
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable Long id, @RequestBody User updatedDoctor) {
        try {
            User updatedDoctorResponse = doctorService.updateDoctor(id, updatedDoctor);
            return ResponseEntity.ok(updatedDoctorResponse);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Deletes a doctor by ID.
     *
     * @param id the ID of the doctor to delete.
     * @return HTTP status 204 (No Content) if successful, or an error message with HTTP status 400 (Bad Request).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Retrieves all doctors.
     *
     * @return a list of doctors as UserDTOs with HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllDoctors() {
        List<UserDTO> doctorDTOs = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctorDTOs);
    }

    /**
     * Retrieves a doctor by ID.
     *
     * @param id the ID of the doctor to retrieve.
     * @return the requested doctor as a UserDTO with HTTP status 200 (OK), or an error message with HTTP status 400 (Bad Request).
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable Long id) {
        try {
            UserDTO doctorDTO = doctorService.getDoctorById(id);
            return ResponseEntity.ok(doctorDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Retrieves a doctor by their unique ID.
     *
     * @param uniqueId the unique ID of the doctor to retrieve.
     * @return the requested doctor as a User entity with HTTP status 200 (OK), or an error message with HTTP status 400 (Bad Request).
     */
    @GetMapping("/unique/{uniqueId}")
    public ResponseEntity<?> getDoctorByUniqueId(@PathVariable String uniqueId) {
        try {
            User doctor = doctorService.getDoctorByUniqueId(uniqueId);
            return ResponseEntity.ok(doctor);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Retrieves the specializations of a doctor by their ID.
     *
     * @param id the ID of the doctor.
     * @return a list of the doctor's specializations as SpecializationDTOs with HTTP status 200 (OK).
     */
    @GetMapping("/{id}/specializations")
    public ResponseEntity<List<SpecializationDTO>> getDoctorSpecializations(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorSpecializations(id));
    }

    /**
     * Adds a specialization to a doctor.
     *
     * @param id the ID of the doctor.
     * @param specializationDTO the details of the specialization to add.
     * @return the updated doctor as a UserDTO with HTTP status 200 (OK), or an error message with HTTP status 400 (Bad Request).
     */
    @PostMapping("/{id}/specializations")
    public ResponseEntity<?> addSpecializationToDoctor(
            @PathVariable Long id,
            @RequestBody SpecializationDTO specializationDTO) {
        try {
            UserDTO updatedDoctor = doctorService.addSpecializationToDoctor(id, specializationDTO);
            return ResponseEntity.ok(updatedDoctor);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Retrieves the appointments of a doctor by their ID.
     *
     * @param id the ID of the doctor.
     * @return a list of the doctor's appointments as AppointmentDTOs with HTTP status 200 (OK).
     */
    @GetMapping("/{id}/appointments")
    public ResponseEntity<List<AppointmentDTO>> getDoctorAppointments(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorAppointments(id));
    }

    /**
     * Retrieves all personal doctors.
     *
     * @return a list of all personal doctors as User entities with HTTP status 200 (OK).
     */
    @GetMapping("/personal-doctors")
    public ResponseEntity<List<User>> getAllPersonalDoctors() {
        return ResponseEntity.ok(doctorService.getAllPersonalDoctors());
    }
}


