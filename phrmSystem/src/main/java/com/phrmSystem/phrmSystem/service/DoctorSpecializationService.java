package com.phrmSystem.phrmSystem.service;

import com.phrmSystem.phrmSystem.dto.DoctorSpecializationDTO;

import java.util.List;

public interface DoctorSpecializationService {
    DoctorSpecializationDTO getDoctorSpecializationById(Long id);
    List<DoctorSpecializationDTO> getAllDoctorSpecializations();
    DoctorSpecializationDTO createDoctorSpecialization(DoctorSpecializationDTO doctorSpecializationDTO);
    DoctorSpecializationDTO updateDoctorSpecialization(Long id, DoctorSpecializationDTO doctorSpecializationDTO);
    void deleteDoctorSpecialization(Long id);
}
