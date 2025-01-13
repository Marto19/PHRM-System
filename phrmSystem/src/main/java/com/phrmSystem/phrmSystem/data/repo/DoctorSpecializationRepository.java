package com.phrmSystem.phrmSystem.data.repo;

import com.phrmSystem.phrmSystem.data.entity.DoctorSpecialization;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorSpecializationRepository extends JpaRepository<DoctorSpecialization, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO doctor_specializations (doctor_id, specialization_id) VALUES (:doctorId, :specializationId)", nativeQuery = true)
    void assignSpecializationToDoctor(@Param("doctorId") Long doctorId, @Param("specializationId") Long specializationId);
}

