package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.*;
import com.phrmSystem.phrmSystem.data.repo.*;
import com.phrmSystem.phrmSystem.dto.DiagnosisDTO;
import com.phrmSystem.phrmSystem.service.DiagnosisService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of DiagnosisService for managing diagnoses and their relationships with other entities.
 */
@Service
@Transactional
public class DiagnosisServiceImpl implements DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final SickDayRepository sickDayRepository;
    private final DoctorAppointmentRepository doctorAppointmentRepository;
    private final MedicineRepository medicineRepository;

    public DiagnosisServiceImpl(DiagnosisRepository diagnosisRepository,
                                SickDayRepository sickDayRepository,
                                DoctorAppointmentRepository doctorAppointmentRepository,
                                MedicineRepository medicineRepository) {
        this.diagnosisRepository = diagnosisRepository;
        this.sickDayRepository = sickDayRepository;
        this.doctorAppointmentRepository = doctorAppointmentRepository;
        this.medicineRepository = medicineRepository;
    }

    /**
     * Creates a new diagnosis.
     *
     * @param dto the DTO containing the diagnosis details.
     * @return the created DiagnosisDTO.
     * @throws RuntimeException if any validation fails.
     */
    @Override
    public DiagnosisDTO createDiagnosis(DiagnosisDTO dto) {
        validateDiagnosisDTO(dto);

        Diagnosis diagnosis = new Diagnosis();
        populateDiagnosisFromDTO(diagnosis, dto);

        Diagnosis savedDiagnosis = diagnosisRepository.save(diagnosis);
        return mapToDTO(savedDiagnosis);
    }

    /**
     * Updates an existing diagnosis by its ID.
     *
     * @param id  the ID of the diagnosis to update.
     * @param dto the DTO containing updated details.
     * @return the updated DiagnosisDTO.
     * @throws RuntimeException if the diagnosis does not exist or validation fails.
     */
    @Override
    public DiagnosisDTO updateDiagnosis(Long id, DiagnosisDTO dto) {
        validateDiagnosisDTO(dto);

        Diagnosis diagnosis = diagnosisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Diagnosis not found with ID: " + id));

        populateDiagnosisFromDTO(diagnosis, dto);

        Diagnosis updatedDiagnosis = diagnosisRepository.save(diagnosis);
        return mapToDTO(updatedDiagnosis);
    }

    /**
     * Deletes a diagnosis by its ID.
     *
     * @param id the ID of the diagnosis to delete.
     * @throws RuntimeException if the diagnosis does not exist or has active dependencies.
     */
    @Override
    public void deleteDiagnosis(Long id) {
        if (!diagnosisRepository.existsById(id)) {
            throw new RuntimeException("Diagnosis not found with ID: " + id);
        }

        try {
            diagnosisRepository.deleteById(id);
        } catch (Exception ex) {
            if (ex.getMessage().contains("constraint")) {
                throw new RuntimeException("Cannot delete diagnosis due to active dependencies.");
            }
            throw ex;
        }
    }

    /**
     * Retrieves all diagnoses.
     *
     * @return a list of DiagnosisDTOs.
     */
    @Override
    public List<DiagnosisDTO> getAllDiagnoses() {
        return diagnosisRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a diagnosis by its ID.
     *
     * @param id the ID of the diagnosis to retrieve.
     * @return the DiagnosisDTO representation.
     * @throws RuntimeException if the diagnosis does not exist.
     */
    @Override
    public DiagnosisDTO getDiagnosisById(Long id) {
        Diagnosis diagnosis = diagnosisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Diagnosis not found with ID: " + id));
        return mapToDTO(diagnosis);
    }

    /**
     * Validates the DiagnosisDTO.
     *
     * @param dto the DTO to validate.
     * @throws RuntimeException if any required field is invalid.
     */
    private void validateDiagnosisDTO(DiagnosisDTO dto) {
        if (dto.getDiagnosisName() == null || dto.getDiagnosisName().isBlank()) {
            throw new RuntimeException("Diagnosis name cannot be null or blank.");
        }
    }

    /**
     * Populates a Diagnosis entity from a DTO.
     *
     * @param diagnosis the Diagnosis entity to populate.
     * @param dto       the DTO containing details.
     */
    private void populateDiagnosisFromDTO(Diagnosis diagnosis, DiagnosisDTO dto) {
        diagnosis.setDiagnosisName(dto.getDiagnosisName());
        diagnosis.setDiagnosisDescription(dto.getDiagnosisDescription());

        if (dto.getSickDayIds() != null) {
            Set<SickDay> sickDays = dto.getSickDayIds().stream()
                    .map(id -> sickDayRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Sick Day not found with ID: " + id)))
                    .collect(Collectors.toSet());
            diagnosis.setSickDays(sickDays);
        }

        if (dto.getDoctorAppointmentId() != null) {
            DoctorAppointment doctorAppointment = doctorAppointmentRepository.findById(dto.getDoctorAppointmentId())
                    .orElseThrow(() -> new RuntimeException("Doctor Appointment not found with ID: " + dto.getDoctorAppointmentId()));
            diagnosis.setDoctorAppointment(doctorAppointment);
        }

        if (dto.getMedicineIds() != null) {
            Set<Medicine> medicines = dto.getMedicineIds().stream()
                    .map(id -> medicineRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Medicine not found with ID: " + id)))
                    .collect(Collectors.toSet());
            diagnosis.setMedicine(medicines);
        }
    }

    /**
     * Maps a Diagnosis entity to its DTO representation.
     *
     * @param diagnosis the Diagnosis entity.
     * @return the corresponding DiagnosisDTO.
     */
    private DiagnosisDTO mapToDTO(Diagnosis diagnosis) {
        DiagnosisDTO dto = new DiagnosisDTO();
        dto.setId(diagnosis.getId());
        dto.setDiagnosisName(diagnosis.getDiagnosisName());
        dto.setDiagnosisDescription(diagnosis.getDiagnosisDescription());

        if (diagnosis.getSickDays() != null) {
            dto.setSickDayIds(diagnosis.getSickDays().stream()
                    .map(SickDay::getId)
                    .collect(Collectors.toSet()));
        }

        if (diagnosis.getDoctorAppointment() != null) {
            dto.setDoctorAppointmentId(diagnosis.getDoctorAppointment().getId());
        }

        if (diagnosis.getMedicine() != null) {
            dto.setMedicineIds(diagnosis.getMedicine().stream()
                    .map(Medicine::getId)
                    .collect(Collectors.toSet()));
        }

        return dto;
    }
}
