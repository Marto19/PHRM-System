package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.PatientIllnessHistory;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.repo.PatientIllnessHistoryRepository;
import com.phrmSystem.phrmSystem.data.repo.UserRepository;
import com.phrmSystem.phrmSystem.dto.PatientIllnessHistoryDTO;
import com.phrmSystem.phrmSystem.service.PatientIllnessHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientIllnessHistoryServiceImpl implements PatientIllnessHistoryService {

    private final PatientIllnessHistoryRepository repository;
    private final UserRepository userRepository;

    public PatientIllnessHistoryServiceImpl(PatientIllnessHistoryRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public List<PatientIllnessHistoryDTO> getAllIllnessHistories() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PatientIllnessHistoryDTO getIllnessHistoryById(Long id) {
        PatientIllnessHistory history = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Illness history not found with id: " + id));
        return mapToDTO(history);
    }

    @Override
    public List<PatientIllnessHistoryDTO> getIllnessHistoriesByPatientId(Long patientId) {
        return repository.findByPatientId(patientId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PatientIllnessHistoryDTO createIllnessHistory(PatientIllnessHistoryDTO dto) {
        User patient = userRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + dto.getPatientId()));

        PatientIllnessHistory history = mapToEntity(dto);
        history.setPatient(patient);

        return mapToDTO(repository.save(history));
    }

    @Override
    public PatientIllnessHistoryDTO updateIllnessHistory(Long id, PatientIllnessHistoryDTO dto) {
        PatientIllnessHistory history = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Illness history not found with id: " + id));

        history.setIllnessName(dto.getIllnessName());
        history.setStartDate(dto.getStartDate());
        history.setEndDate(dto.getEndDate());

        User patient = userRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + dto.getPatientId()));

        history.setPatient(patient);

        return mapToDTO(repository.save(history));
    }

    @Override
    public void deleteIllnessHistory(Long id) {
        repository.deleteById(id);
    }

    private PatientIllnessHistoryDTO mapToDTO(PatientIllnessHistory history) {
        return new PatientIllnessHistoryDTO(
                history.getId(),
                history.getIllnessName(),
                history.getStartDate(),
                history.getEndDate(),
                history.getPatient().getId()
        );
    }

    private PatientIllnessHistory mapToEntity(PatientIllnessHistoryDTO dto) {
        PatientIllnessHistory history = new PatientIllnessHistory();
        history.setIllnessName(dto.getIllnessName());
        history.setStartDate(dto.getStartDate());
        history.setEndDate(dto.getEndDate());
        return history;
    }
}
