package com.phrmSystem.phrmSystem.data.repo;

import com.phrmSystem.phrmSystem.data.entity.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
    List<Diagnosis> findByDiagnosisName(String diagnosisName);

    @Query("SELECT d.diagnosisName, COUNT(p) FROM Diagnosis d JOIN d.sickDays s JOIN s.patient p GROUP BY d.diagnosisName ORDER BY COUNT(p) DESC")
    List<Object[]> findMostCommonDiagnoses();
}