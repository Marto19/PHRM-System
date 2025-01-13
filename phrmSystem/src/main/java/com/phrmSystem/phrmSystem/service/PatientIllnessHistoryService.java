package com.phrmSystem.phrmSystem.service;

import com.phrmSystem.phrmSystem.dto.PatientIllnessHistoryDTO;

import java.util.List;

public interface PatientIllnessHistoryService {
    List<PatientIllnessHistoryDTO> getAllIllnessHistories();
    PatientIllnessHistoryDTO getIllnessHistoryById(Long id);
    List<PatientIllnessHistoryDTO> getIllnessHistoriesByPatientId(Long patientId);
    PatientIllnessHistoryDTO createIllnessHistory(PatientIllnessHistoryDTO dto);
    PatientIllnessHistoryDTO updateIllnessHistory(Long id, PatientIllnessHistoryDTO dto);
    void deleteIllnessHistory(Long id);
}
