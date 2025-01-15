package com.phrmSystem.phrmSystem.service;

import com.phrmSystem.phrmSystem.dto.DiagnosisDTO;

import java.util.List;

public interface DiagnosisService {
    DiagnosisDTO createDiagnosis(DiagnosisDTO dto);
    DiagnosisDTO updateDiagnosis(Long id, DiagnosisDTO dto);
    void deleteDiagnosis(Long id);
    List<DiagnosisDTO> getAllDiagnoses();
    DiagnosisDTO getDiagnosisById(Long id);
}
