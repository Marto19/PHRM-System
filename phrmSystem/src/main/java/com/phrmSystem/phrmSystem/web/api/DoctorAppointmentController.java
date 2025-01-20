package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.data.entity.DoctorAppointment;
import com.phrmSystem.phrmSystem.data.repo.DoctorAppointmentRepository;
import com.phrmSystem.phrmSystem.dto.DoctorAppointmentAllDTO;
import com.phrmSystem.phrmSystem.service.DoctorAppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Controller for managing doctor appointments.
 */
@RestController
@RequestMapping("/api/appointments")
public class DoctorAppointmentController {

    private final DoctorAppointmentService doctorAppointmentService;
    private final DoctorAppointmentRepository doctorAppointmentRepository;

    public DoctorAppointmentController(DoctorAppointmentService doctorAppointmentService, DoctorAppointmentRepository doctorAppointmentRepository) {
        this.doctorAppointmentService = doctorAppointmentService;
        this.doctorAppointmentRepository = doctorAppointmentRepository;
    }

    /**
     * Creates a new doctor appointment.
     *
     * @param appointmentDTO the details of the appointment to create.
     * @return the created DoctorAppointmentAllDTO.
     */
    @PostMapping
    public ResponseEntity<?> createDoctorAppointment(@RequestBody DoctorAppointmentAllDTO appointmentDTO) {
        try {
            return ResponseEntity.ok(doctorAppointmentService.createDoctorAppointment(appointmentDTO));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Updates an existing doctor appointment.
     *
     * @param id               the ID of the appointment to update.
     * @param appointmentDTO   the updated appointment details.
     * @return the updated DoctorAppointmentAllDTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctorAppointment(
            @PathVariable Long id,
            @RequestBody DoctorAppointmentAllDTO appointmentDTO) {
        try {
            return ResponseEntity.ok(doctorAppointmentService.updateDoctorAppointment(id, appointmentDTO));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Retrieves a doctor appointment by its ID.
     *
     * @param id the ID of the appointment to retrieve.
     * @return the DoctorAppointmentAllDTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorAppointmentById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(doctorAppointmentService.getDoctorAppointmentById(id));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Retrieves all doctor appointments.
     *
     * @return a list of DoctorAppointmentAllDTOs.
     */
    @GetMapping
    public ResponseEntity<List<DoctorAppointmentAllDTO>> getAllDoctorAppointments() {
        return ResponseEntity.ok(doctorAppointmentService.getAllDoctorAppointments());
    }

    /**
     * Deletes a doctor appointment by its ID.
     *
     * @param id the ID of the appointment to delete.
     * @return a no-content response on success.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctorAppointment(@PathVariable Long id) {
        try {
            doctorAppointmentService.deleteDoctorAppointment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Retrieves appointments for a specific doctor within a date range.
     *
     * @param doctorId  the ID of the doctor.
     * @param startDate the start date of the range.
     * @param endDate   the end date of the range.
     * @return a list of DoctorAppointment entities.
     */
    @GetMapping("/doctor/{doctorId}/date-range")
    public ResponseEntity<?> getAppointmentsByDoctorAndDateRange(
            @PathVariable Long doctorId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);

            List<DoctorAppointment> appointments = doctorAppointmentRepository.findAppointmentsByDoctorIdAndDateRange(
                    doctorId, start, end);

            return ResponseEntity.ok(appointments);
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().body("Invalid date format or other error: Invalid date range.");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
