package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.*;
import com.phrmSystem.phrmSystem.data.repo.*;
import com.phrmSystem.phrmSystem.dto.DoctorAppointmentAllDTO;
import com.phrmSystem.phrmSystem.mappers.DoctorAppointmentAllMapper;
import com.phrmSystem.phrmSystem.service.DoctorAppointmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of DoctorAppointmentService for managing doctor appointments.
 */
@Service
@Transactional
public class DoctorAppointmentServiceImpl implements DoctorAppointmentService {

    private final DoctorAppointmentRepository doctorAppointmentRepository;
    private final UserRepository userRepository;
    private final PatientIllnessHistoryRepository patientIllnessHistoryRepository;
    private final DiagnosisRepository diagnosisRepository;

    public DoctorAppointmentServiceImpl(
            DoctorAppointmentRepository doctorAppointmentRepository,
            UserRepository userRepository,
            PatientIllnessHistoryRepository patientIllnessHistoryRepository,
            DiagnosisRepository diagnosisRepository
    ) {
        this.doctorAppointmentRepository = doctorAppointmentRepository;
        this.userRepository = userRepository;
        this.patientIllnessHistoryRepository = patientIllnessHistoryRepository;
        this.diagnosisRepository = diagnosisRepository;
    }

    /**
     * Creates a new doctor appointment.
     *
     * @param appointmentDTO the DTO containing appointment details.
     * @return the created DoctorAppointmentAllDTO.
     * @throws RuntimeException if any required fields are missing or entities are not found.
     */
    @Override
    public DoctorAppointmentAllDTO createDoctorAppointment(DoctorAppointmentAllDTO appointmentDTO) {
        validateAppointmentDTO(appointmentDTO);

        DoctorAppointment appointment = new DoctorAppointment();
        appointment.setDate(appointmentDTO.getDate());

        appointment.setPatient(userRepository.findById(appointmentDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + appointmentDTO.getPatientId())));
        appointment.setDoctor(userRepository.findById(appointmentDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + appointmentDTO.getDoctorId())));

        if (appointmentDTO.getPatientIllnessHistoryId() != null) {
            PatientIllnessHistory history = patientIllnessHistoryRepository.findById(appointmentDTO.getPatientIllnessHistoryId())
                    .orElseThrow(() -> new RuntimeException("Patient Illness History not found with ID: " + appointmentDTO.getPatientIllnessHistoryId()));
            appointment.setPatientIllnessHistory(history);
        }

        if (appointmentDTO.getDiagnosisIds() != null && !appointmentDTO.getDiagnosisIds().isEmpty()) {
            Set<Diagnosis> diagnoses = mapDiagnosisIdsToEntities(appointmentDTO.getDiagnosisIds());
            appointment.setDiagnosis(diagnoses);
        }

        DoctorAppointment savedAppointment = doctorAppointmentRepository.save(appointment);
        return mapToDTO(savedAppointment);
    }

    /**
     * Updates an existing doctor appointment.
     *
     * @param id             the ID of the appointment to update.
     * @param appointmentDTO the DTO containing updated appointment details.
     * @return the updated DoctorAppointmentAllDTO.
     * @throws RuntimeException if the appointment or related entities are not found.
     */
    @Override
    public DoctorAppointmentAllDTO updateDoctorAppointment(Long id, DoctorAppointmentAllDTO appointmentDTO) {
        validateAppointmentDTO(appointmentDTO);

        DoctorAppointment appointment = doctorAppointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor Appointment not found with ID: " + id));

        appointment.setDate(appointmentDTO.getDate());

        if (appointmentDTO.getPatientId() != null) {
            User patient = userRepository.findById(appointmentDTO.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + appointmentDTO.getPatientId()));
            appointment.setPatient(patient);
        }

        if (appointmentDTO.getDoctorId() != null) {
            User doctor = userRepository.findById(appointmentDTO.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + appointmentDTO.getDoctorId()));
            appointment.setDoctor(doctor);
        }

        if (appointmentDTO.getPatientIllnessHistoryId() != null) {
            PatientIllnessHistory history = patientIllnessHistoryRepository.findById(appointmentDTO.getPatientIllnessHistoryId())
                    .orElseThrow(() -> new RuntimeException("Patient Illness History not found with ID: " + appointmentDTO.getPatientIllnessHistoryId()));
            appointment.setPatientIllnessHistory(history);
        }

        if (appointmentDTO.getDiagnosisIds() != null && !appointmentDTO.getDiagnosisIds().isEmpty()) {
            Set<Diagnosis> diagnoses = mapDiagnosisIdsToEntities(appointmentDTO.getDiagnosisIds());
            appointment.setDiagnosis(diagnoses);
        }

        DoctorAppointment updatedAppointment = doctorAppointmentRepository.save(appointment);
        return mapToDTO(updatedAppointment);
    }

    /**
     * Retrieves a doctor appointment by ID.
     *
     * @param id the ID of the appointment.
     * @return the corresponding DoctorAppointmentAllDTO.
     * @throws RuntimeException if the appointment is not found.
     */
    @Override
    public DoctorAppointmentAllDTO getDoctorAppointmentById(Long id) {
        DoctorAppointment appointment = doctorAppointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor Appointment not found with ID: " + id));
        return mapToDTO(appointment);
    }

    /**
     * Retrieves all doctor appointments.
     *
     * @return a list of DoctorAppointmentAllDTOs.
     */
    @Override
    public List<DoctorAppointmentAllDTO> getAllDoctorAppointments() {
        return doctorAppointmentRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a doctor appointment by ID.
     *
     * @param id the ID of the appointment to delete.
     * @throws RuntimeException if the appointment is not found or cannot be deleted.
     */
    @Override
    public void deleteDoctorAppointment(Long id) {
        if (!doctorAppointmentRepository.existsById(id)) {
            throw new RuntimeException("Doctor Appointment not found with ID: " + id);
        }

        try {
            doctorAppointmentRepository.deleteById(id);
        } catch (Exception ex) {
            if (ex.getMessage().contains("constraint")) {
                throw new RuntimeException("Cannot delete appointment due to active dependencies.");
            }
            throw ex;
        }
    }

    /**
     * Validates the DoctorAppointmentAllDTO for required fields.
     *
     * @param appointmentDTO the DTO to validate.
     * @throws RuntimeException if required fields are missing.
     */
    private void validateAppointmentDTO(DoctorAppointmentAllDTO appointmentDTO) {
        if (appointmentDTO.getDate() == null) {
            throw new RuntimeException("Appointment date cannot be null.");
        }
        if (appointmentDTO.getPatientId() == null) {
            throw new RuntimeException("Patient ID must be provided.");
        }
        if (appointmentDTO.getDoctorId() == null) {
            throw new RuntimeException("Doctor ID must be provided.");
        }
    }

    /**
     * Maps a set of diagnosis IDs to Diagnosis entities.
     *
     * @param diagnosisIds the set of diagnosis IDs.
     * @return the corresponding Diagnosis entities.
     * @throws RuntimeException if any diagnosis ID is not found.
     */
    private Set<Diagnosis> mapDiagnosisIdsToEntities(Set<Long> diagnosisIds) {
        return diagnosisIds.stream()
                .map(id -> diagnosisRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Diagnosis not found with ID: " + id)))
                .collect(Collectors.toSet());
    }

    /**
     * Maps a DoctorAppointment entity to a DoctorAppointmentAllDTO.
     *
     * @param appointment the DoctorAppointment entity.
     * @return the corresponding DoctorAppointmentAllDTO.
     */
    private DoctorAppointmentAllDTO mapToDTO(DoctorAppointment appointment) {
        return new DoctorAppointmentAllDTO(
                appointment.getId(),
                appointment.getDate(),
                appointment.getPatient() != null ? appointment.getPatient().getId() : null,
                appointment.getDoctor() != null ? appointment.getDoctor().getId() : null,
                appointment.getPatientIllnessHistory() != null ? appointment.getPatientIllnessHistory().getId() : null,
                appointment.getDiagnosis() != null
                        ? appointment.getDiagnosis().stream().map(Diagnosis::getId).collect(Collectors.toSet())
                        : null
        );
    }
}
