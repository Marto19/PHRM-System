package com.phrmSystem.phrmSystem.data.repo;

import com.phrmSystem.phrmSystem.data.entity.DoctorAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DoctorAppointmentRepository extends JpaRepository<DoctorAppointment, Long> {

    // Find appointments by doctor ID
    @Query("SELECT da FROM DoctorAppointment da WHERE da.doctor.id = :doctorId")
    List<DoctorAppointment> findAppointmentsByDoctorId(Long doctorId);

    // Find appointments by patient ID
    @Query("SELECT da FROM DoctorAppointment da WHERE da.patient.id = :patientId")
    List<DoctorAppointment> findAppointmentsByPatientId(Long patientId);

    // Find appointments within a specific date range
    @Query("SELECT da FROM DoctorAppointment da WHERE da.date BETWEEN :startDate AND :endDate")
    List<DoctorAppointment> findAppointmentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    //---------------------------------------------------------------------------------


    @Query("SELECT da.doctor, COUNT(da) FROM DoctorAppointment da GROUP BY da.doctor")
    List<Object[]> countAppointmentsPerDoctor();

    @Query("SELECT da FROM DoctorAppointment da WHERE da.doctor.id = :doctorId AND da.date BETWEEN :startDate AND :endDate")
    List<DoctorAppointment> findAppointmentsByDoctorIdAndDateRange(
            @Param("doctorId") Long doctorId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
