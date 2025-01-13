package com.phrmSystem.phrmSystem.data.repo;

import com.phrmSystem.phrmSystem.data.entity.PatientIllnessHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientIllnessHistoryRepository extends JpaRepository<PatientIllnessHistory, Long> {

    // Custom query to find all illness history for a specific patient
    List<PatientIllnessHistory> findByPatientId(Long patientId);
}
