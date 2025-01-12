package com.phrmSystem.phrmSystem.data.repo;

import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<User, Long> {

    // Find all patients
    @Query("""
    SELECT u
    FROM User u
    JOIN FETCH u.role r
    WHERE r.roleName = :roleName
""")
    List<User> findAllUsersByRoleName(@Param("roleName") String roleName);

    // Find a patient by unique identification
    @Query("SELECT u FROM User u WHERE u.uniqueIdentification = :uniqueIdentification")
    User findPatientByUniqueIdentification(String uniqueIdentification);

    // Find all patients with insurance paid in the last 6 months
    @Query("SELECT u FROM User u WHERE u.insurancePaidLast6Months = TRUE")
    List<User> findPatientsWithInsurancePaid();

    // Find a patient's illness history
    @Query("SELECT u.patientIllnessHistory FROM User u WHERE u.id = :patientId")
    List<?> findPatientIllnessHistory(Long patientId);
}
