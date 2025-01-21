package com.phrmSystem.phrmSystem.data.repo;

import com.phrmSystem.phrmSystem.data.entity.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    /**
     * Finds diagnoses by their name.
     *
     * @param diagnosisName the name of the diagnosis.
     * @return a list of diagnoses with the specified name.
     */
    List<Diagnosis> findByDiagnosisName(String diagnosisName);

    /**
     * Retrieves the most common diagnoses along with their count.
     *
     * @return a list of objects where each object contains the diagnosis name and the number of occurrences.
     */
    @Query("""
        SELECT d.diagnosisName, COUNT(s)
        FROM Diagnosis d 
        JOIN d.sickDays s 
        GROUP BY d.diagnosisName 
        ORDER BY COUNT(s) DESC
    """)
    List<Object[]> findMostCommonDiagnoses();

    /**
     * Finds all diagnoses associated with a specific doctor appointment.
     *
     * @param appointmentId the ID of the doctor appointment.
     * @return a list of diagnoses linked to the specified doctor appointment.
     */
    @Query("""
        SELECT d 
        FROM Diagnosis d 
        JOIN d.doctorAppointment da 
        WHERE da.id = :appointmentId
    """)
    List<Diagnosis> findDiagnosesByDoctorAppointmentId(Long appointmentId);
}
