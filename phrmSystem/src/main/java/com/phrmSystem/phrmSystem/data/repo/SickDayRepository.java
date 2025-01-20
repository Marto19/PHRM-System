package com.phrmSystem.phrmSystem.data.repo;

import com.phrmSystem.phrmSystem.data.entity.SickDay;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SickDayRepository extends JpaRepository<SickDay, Long> {
    @Query("SELECT s FROM SickDay s WHERE s.startDate BETWEEN :startDate AND :endDate")
    List<SickDay> findSickDaysWithinPeriod(LocalDate startDate, LocalDate endDate);

    @EntityGraph(attributePaths = {"patient", "doctor", "diagnosis"})
    @Query("SELECT s FROM SickDay s")
    List<SickDay> findAllWithRelations();

    //---------------------------------------------------------------------------
    @Query("SELECT FUNCTION('MONTH', s.startDate), COUNT(s) FROM SickDay s GROUP BY FUNCTION('MONTH', s.startDate) ORDER BY COUNT(s) DESC")
    List<Object[]> findMonthWithMostSickLeaves();

    @Query("SELECT s.doctor, COUNT(s) FROM SickDay s GROUP BY s.doctor ORDER BY COUNT(s) DESC")
    List<Object[]> findDoctorsWithMostSickLeaves();

}
