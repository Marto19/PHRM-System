package com.phrmSystem.phrmSystem.service;

import com.phrmSystem.phrmSystem.dto.SickDayDTO;

import java.util.List;

public interface SickDayService {
    SickDayDTO getSickDayById(Long id);
    List<SickDayDTO> getAllSickDays();
    SickDayDTO createSickDay(SickDayDTO sickDayDTO);
    SickDayDTO updateSickDay(Long id, SickDayDTO sickDayDTO);
    void deleteSickDay(Long id);
    List<Object[]> getMonthWithMostSickLeaves();
    List<Object[]> getDoctorsWithMostSickLeaves();
}
