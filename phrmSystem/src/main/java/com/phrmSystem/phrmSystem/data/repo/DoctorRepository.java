package com.phrmSystem.phrmSystem.data.repo;

import com.phrmSystem.phrmSystem.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<User, Long> {

    // Find all doctors
    @Query("SELECT u FROM User u JOIN u.role r WHERE r.roleName = 'DOCTOR'")
    List<User> findAllDoctors();

    // Find all personal doctors
    @Query("SELECT u FROM User u WHERE u.isPersonalDoctor = TRUE")
    List<User> findAllPersonalDoctors();

    // Find doctor by unique ID
    @Query("SELECT u FROM User u WHERE u.uniqueId = :uniqueId")
    User findDoctorByUniqueId(String uniqueId);

    // Find doctor specializations
    @Query("SELECT u FROM User u JOIN u.specializations s WHERE u.id = :doctorId")
    List<User> findDoctorSpecializations(Long doctorId);
}
