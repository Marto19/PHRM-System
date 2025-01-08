package com.phrmSystem.phrmSystem.data.repo;

import com.phrmSystem.phrmSystem.data.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // Find doctors by specialization
    @Query("SELECT d FROM Doctor d JOIN d.specializations s WHERE s.specialization LIKE %:specialization%")
    List<Doctor> findBySpecializationsContaining(@Param("specialization") String specialization);

    // Find personal doctors
    List<Doctor> findByIsPersonalDoctorTrue();

    // Find a doctor by unique ID
    Optional<Doctor> findByUniqueId(String uniqueId);

}
