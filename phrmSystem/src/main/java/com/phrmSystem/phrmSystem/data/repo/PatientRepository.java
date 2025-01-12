package com.phrmSystem.phrmSystem.data.repo;

import com.phrmSystem.phrmSystem.data.entity.Doctor;
import com.phrmSystem.phrmSystem.data.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

//    List<Patient> findByPersonalDoctor_Id(Long doctorId);

    @Query("SELECT COUNT(p) FROM Patient p WHERE p.personalDoctor.id = :doctorId")
    int countByPersonalDoctorId(Long doctorId);

//    @Query("SELECT p FROM Patient p WHERE p.uniqueIdentification = :uniqueIdentification")
//    Patient findByUniqueIdentification(String uniqueIdentification);

    @Query("SELECT p FROM Patient p WHERE p.personalDoctor.id = :doctorId")
    List<Patient> findByPersonalDoctorId(@Param("doctorId") Long doctorId);

    Optional<Patient> findByUniqueIdentification(String uniqueIdentification);



}
