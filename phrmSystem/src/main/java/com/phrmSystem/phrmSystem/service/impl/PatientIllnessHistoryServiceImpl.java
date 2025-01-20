package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.PatientIllnessHistory;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.repo.PatientIllnessHistoryRepository;
import com.phrmSystem.phrmSystem.data.repo.UserRepository;
import com.phrmSystem.phrmSystem.dto.PatientIllnessHistoryDTO;
import com.phrmSystem.phrmSystem.service.PatientIllnessHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing Patient Illness Histories.
 */
@Service
public class PatientIllnessHistoryServiceImpl implements PatientIllnessHistoryService {

    private final PatientIllnessHistoryRepository repository;
    private final UserRepository userRepository;

    public PatientIllnessHistoryServiceImpl(PatientIllnessHistoryRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all patient illness histories.
     *
     * @return a list of PatientIllnessHistoryDTO objects.
     */
    @Override
    public List<PatientIllnessHistoryDTO> getAllIllnessHistories() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific illness history by its ID.
     *
     * @param id the ID of the illness history.
     * @return the corresponding PatientIllnessHistoryDTO.
     * @throws RuntimeException if the illness history is not found.
     */
    @Override
    public PatientIllnessHistoryDTO getIllnessHistoryById(Long id) {
        PatientIllnessHistory history = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Illness history not found with id: " + id));
        return mapToDTO(history);
    }

    /**
     * Retrieves all illness histories for a specific patient by patient ID.
     *
     * @param patientId the ID of the patient.
     * @return a list of PatientIllnessHistoryDTO objects for the patient.
     */
    @Override
    public List<PatientIllnessHistoryDTO> getIllnessHistoriesByPatientId(Long patientId) {
        return repository.findByPatientId(patientId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new illness history for a patient.
     *
     * @param dto the details of the illness history.
     * @return the created PatientIllnessHistoryDTO.
     * @throws RuntimeException if the patient is not found.
     */
    @Override
    @Transactional
    public PatientIllnessHistoryDTO createIllnessHistory(PatientIllnessHistoryDTO dto) {
        validateIllnessHistoryDTO(dto);

        User patient = userRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + dto.getPatientId()));

        PatientIllnessHistory history = mapToEntity(dto);
        history.setPatient(patient);

        return mapToDTO(repository.save(history));
    }

    /**
     * Updates an existing illness history.
     *
     * @param id  the ID of the illness history to update.
     * @param dto the updated details of the illness history.
     * @return the updated PatientIllnessHistoryDTO.
     * @throws RuntimeException if the illness history or patient is not found.
     */
    @Override
    @Transactional
    public PatientIllnessHistoryDTO updateIllnessHistory(Long id, PatientIllnessHistoryDTO dto) {
        validateIllnessHistoryDTO(dto);

        PatientIllnessHistory history = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Illness history not found with id: " + id));

        User patient = userRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + dto.getPatientId()));

        history.setIllnessName(dto.getIllnessName());
        history.setStartDate(dto.getStartDate());
        history.setEndDate(dto.getEndDate());
        history.setPatient(patient);

        return mapToDTO(repository.save(history));
    }

    /**
     * Deletes an illness history by its ID.
     *
     * @param id the ID of the illness history to delete.
     * @throws RuntimeException if the illness history is not found.
     */
    @Override
    @Transactional
    public void deleteIllnessHistory(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Illness history not found with id: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Maps a PatientIllnessHistory entity to a PatientIllnessHistoryDTO.
     *
     * @param history the PatientIllnessHistory entity.
     * @return the mapped PatientIllnessHistoryDTO.
     */
    private PatientIllnessHistoryDTO mapToDTO(PatientIllnessHistory history) {
        return new PatientIllnessHistoryDTO(
                history.getId(),
                history.getIllnessName(),
                history.getStartDate(),
                history.getEndDate(),
                history.getPatient() != null ? history.getPatient().getId() : null
        );
    }

    /**
     * Maps a PatientIllnessHistoryDTO to a PatientIllnessHistory entity.
     *
     * @param dto the PatientIllnessHistoryDTO.
     * @return the mapped PatientIllnessHistory entity.
     */
    private PatientIllnessHistory mapToEntity(PatientIllnessHistoryDTO dto) {
        PatientIllnessHistory history = new PatientIllnessHistory();
        history.setIllnessName(dto.getIllnessName());
        history.setStartDate(dto.getStartDate());
        history.setEndDate(dto.getEndDate());
        return history;
    }

    /**
     * Validates the details of a PatientIllnessHistoryDTO.
     *
     * @param dto the PatientIllnessHistoryDTO to validate.
     * @throws IllegalArgumentException if validation fails.
     */
    private void validateIllnessHistoryDTO(PatientIllnessHistoryDTO dto) {
        if (dto.getIllnessName() == null || dto.getIllnessName().isBlank()) {
            throw new IllegalArgumentException("Illness name is required.");
        }
        if (dto.getPatientId() == null) {
            throw new IllegalArgumentException("Patient ID is required.");
        }
        if (dto.getStartDate() != null && dto.getEndDate() != null && dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }
    }
}
