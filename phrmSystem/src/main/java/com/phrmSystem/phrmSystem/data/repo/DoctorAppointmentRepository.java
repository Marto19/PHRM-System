package com.phrmSystem.phrmSystem.data.repo;

import com.phrmSystem.phrmSystem.data.entity.DoctorAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DoctorAppointmentRepository extends JpaRepository<DoctorAppointment, Long> {
    @Query("SELECT a FROM DoctorAppointment a WHERE a.date BETWEEN :startDate AND :endDate")
    List<DoctorAppointment> findAppointmentsWithinPeriod(LocalDate startDate, LocalDate endDate);

    @Query("SELECT COUNT(a) FROM DoctorAppointment a WHERE a.doctor.id = :doctorId")
    int countAppointmentsByDoctorId(Long doctorId);
}
