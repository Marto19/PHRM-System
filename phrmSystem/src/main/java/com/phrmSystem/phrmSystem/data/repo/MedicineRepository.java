package com.phrmSystem.phrmSystem.data.repo;

import com.phrmSystem.phrmSystem.data.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    @Query("SELECT m FROM Medicine m JOIN m.diagnosis d WHERE d.id = :diagnosisId")
    List<Medicine> findMedicinesByDiagnosisId(Long diagnosisId);
}
