package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.Diagnosis;
import com.phrmSystem.phrmSystem.data.entity.SickDay;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.repo.DiagnosisRepository;
import com.phrmSystem.phrmSystem.data.repo.SickDayRepository;
import com.phrmSystem.phrmSystem.data.repo.UserRepository;
import com.phrmSystem.phrmSystem.dto.SickDayDTO;
import com.phrmSystem.phrmSystem.service.SickDayService;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the SickDayService interface, responsible for managing
 * SickDay entities and their operations.
 */
@Service
public class SickDayServiceImpl implements SickDayService {

    private final SickDayRepository sickDayRepository;
    private final UserRepository userRepository;
    private final DiagnosisRepository diagnosisRepository;

    /**
     * Constructs a SickDayServiceImpl with required dependencies.
     *
     * @param sickDayRepository   repository for managing SickDay entities.
     * @param userRepository      repository for managing User entities.
     * @param diagnosisRepository repository for managing Diagnosis entities.
     */
    public SickDayServiceImpl(SickDayRepository sickDayRepository, UserRepository userRepository, DiagnosisRepository diagnosisRepository) {
        this.sickDayRepository = sickDayRepository;
        this.userRepository = userRepository;
        this.diagnosisRepository = diagnosisRepository;
    }

    /**
     * Retrieves a SickDay by its ID.
     *
     * @param id the ID of the SickDay to retrieve.
     * @return the SickDayDTO representation of the SickDay.
     * @throws RuntimeException if no SickDay is found with the given ID.
     */
    @Override
    public SickDayDTO getSickDayById(Long id) {
        return sickDayRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("SickDay not found with id: " + id));
    }

    /**
     * Retrieves all SickDays in the system.
     *
     * @return a list of SickDayDTOs.
     */
    @Override
    public List<SickDayDTO> getAllSickDays() {
        return sickDayRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new SickDay based on the provided SickDayDTO.
     *
     * @param sickDayDTO the data transfer object containing SickDay details.
     * @return the created SickDay as a SickDayDTO.
     * @throws IllegalArgumentException if any validation fails.
     */
    @Override
    public SickDayDTO createSickDay(SickDayDTO sickDayDTO) {
        validateSickDayDTO(sickDayDTO);
        calculateNumberOfDays(sickDayDTO);

        SickDay sickDay = mapToEntity(sickDayDTO);
        SickDay savedSickDay = sickDayRepository.save(sickDay);
        return mapToDTO(savedSickDay);
    }

    /**
     * Updates an existing SickDay.
     *
     * @param id          the ID of the SickDay to update.
     * @param sickDayDTO  the new details for the SickDay.
     * @return the updated SickDay as a SickDayDTO.
     * @throws RuntimeException if no SickDay is found with the given ID.
     * @throws IllegalArgumentException if any validation fails.
     */
    @Override
    public SickDayDTO updateSickDay(Long id, SickDayDTO sickDayDTO) {
        SickDay existingSickDay = sickDayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SickDay not found with id: " + id));

        validateSickDayDTO(sickDayDTO);
        calculateNumberOfDays(sickDayDTO);

        existingSickDay.setStartDate(sickDayDTO.getStartDate());
        existingSickDay.setEndDate(sickDayDTO.getEndDate());
        existingSickDay.setNumberOfDays(sickDayDTO.getNumberOfDays());

        if (sickDayDTO.getPatientId() != null) {
            User patient = findUserById(sickDayDTO.getPatientId(), "Patient");
            existingSickDay.setPatient(patient);
        }

        if (sickDayDTO.getDoctorId() != null) {
            User doctor = findUserById(sickDayDTO.getDoctorId(), "Doctor");
            existingSickDay.setDoctor(doctor);
        }

        if (sickDayDTO.getDiagnosisIds() != null && !sickDayDTO.getDiagnosisIds().isEmpty()) {
            Set<Diagnosis> diagnoses = mapDiagnosisIdsToEntities(sickDayDTO.getDiagnosisIds());
            existingSickDay.setDiagnosis(diagnoses);
        }

        SickDay updatedSickDay = sickDayRepository.save(existingSickDay);
        return mapToDTO(updatedSickDay);
    }

    /**
     * Deletes a SickDay by its ID.
     *
     * @param id the ID of the SickDay to delete.
     * @throws RuntimeException if no SickDay is found with the given ID.
     */
    @Override
    public void deleteSickDay(Long id) {
        SickDay sickDay = sickDayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SickDay not found with id: " + id));
        sickDayRepository.delete(sickDay);
    }

    /**
     * Validates the SickDayDTO for required fields and logical correctness.
     *
     * @param sickDayDTO the SickDayDTO to validate.
     * @throws IllegalArgumentException if validation fails.
     */
    private void validateSickDayDTO(SickDayDTO sickDayDTO) {
        if (sickDayDTO.getStartDate() == null || sickDayDTO.getEndDate() == null) {
            throw new IllegalArgumentException("Start and end dates must not be null.");
        }
        if (sickDayDTO.getStartDate().isAfter(sickDayDTO.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }
        if (sickDayDTO.getPatientId() == null) {
            throw new IllegalArgumentException("Patient ID must not be null.");
        }
        if (sickDayDTO.getDoctorId() == null) {
            throw new IllegalArgumentException("Doctor ID must not be null.");
        }
    }

    /**
     * Calculates and sets the number of days between the start and end dates.
     *
     * @param sickDayDTO the SickDayDTO containing the dates.
     */
    private void calculateNumberOfDays(SickDayDTO sickDayDTO) {
        long daysBetween = ChronoUnit.DAYS.between(sickDayDTO.getStartDate(), sickDayDTO.getEndDate());
        sickDayDTO.setNumberOfDays((int) daysBetween);
    }

    /**
     * Retrieves a User by their ID.
     *
     * @param userId the ID of the User.
     * @param role   the role description for the error message.
     * @return the User entity.
     * @throws RuntimeException if no User is found with the given ID.
     */
    private User findUserById(Long userId, String role) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(role + " not found with id: " + userId));
    }

    /**
     * Maps a set of diagnosis IDs to their corresponding Diagnosis entities.
     *
     * @param diagnosisIds the IDs of the diagnoses.
     * @return a set of Diagnosis entities.
     * @throws RuntimeException if any Diagnosis ID is not found.
     */
    private Set<Diagnosis> mapDiagnosisIdsToEntities(Set<Long> diagnosisIds) {
        return diagnosisIds.stream()
                .map(id -> diagnosisRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Diagnosis not found with id: " + id)))
                .collect(Collectors.toSet());
    }

    /**
     * Maps a SickDay entity to a SickDayDTO.
     *
     * @param sickDay the SickDay entity.
     * @return the SickDayDTO representation.
     */
    private SickDayDTO mapToDTO(SickDay sickDay) {
        if (sickDay == null) {
            return null;
        }

        SickDayDTO dto = new SickDayDTO();
        dto.setId(sickDay.getId());
        dto.setStartDate(sickDay.getStartDate());
        dto.setEndDate(sickDay.getEndDate());
        dto.setNumberOfDays(sickDay.getNumberOfDays());
        dto.setDoctorId(sickDay.getDoctor() != null ? sickDay.getDoctor().getId() : null);
        dto.setPatientId(sickDay.getPatient() != null ? sickDay.getPatient().getId() : null);

        // Handle diagnoses gracefully
        dto.setDiagnosisIds(sickDay.getDiagnosis() != null
                ? sickDay.getDiagnosis().stream()
                .map(diagnosis -> diagnosis != null ? diagnosis.getId() : null)
                .filter(id -> id != null) // Skip any null IDs
                .collect(Collectors.toSet())
                : Set.of());
        return dto;
    }


    /**
     * Maps a SickDayDTO to a SickDay entity.
     *
     * @param sickDayDTO the SickDayDTO.
     * @return the SickDay entity.
     */
    private SickDay mapToEntity(SickDayDTO sickDayDTO) {
        SickDay sickDay = new SickDay();
        sickDay.setStartDate(sickDayDTO.getStartDate());
        sickDay.setEndDate(sickDayDTO.getEndDate());
        sickDay.setNumberOfDays(sickDayDTO.getNumberOfDays());

        User patient = findUserById(sickDayDTO.getPatientId(), "Patient");
        sickDay.setPatient(patient);

        User doctor = findUserById(sickDayDTO.getDoctorId(), "Doctor");
        sickDay.setDoctor(doctor);

        if (sickDayDTO.getDiagnosisIds() != null && !sickDayDTO.getDiagnosisIds().isEmpty()) {
            Set<Diagnosis> diagnoses = mapDiagnosisIdsToEntities(sickDayDTO.getDiagnosisIds());
            sickDay.setDiagnosis(diagnoses);
        }

        return sickDay;
    }

    @Override
    public List<Object[]> getMonthWithMostSickLeaves() {
        try {
            return sickDayRepository.findMonthWithMostSickLeaves();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to fetch month with most sick leaves.", ex);
        }
    }

    @Override
    public List<Object[]> getDoctorsWithMostSickLeaves() {
        try {
            return sickDayRepository.findDoctorsWithMostSickLeaves();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to fetch doctors with most sick leaves.", ex);
        }
    }
}
