package com.phrmSystem.phrmSystem.service;

import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.entity.DoctorSpecialization;
import com.phrmSystem.phrmSystem.data.entity.DoctorAppointment;
import com.phrmSystem.phrmSystem.dto.AppointmentDTO;
import com.phrmSystem.phrmSystem.dto.SpecializationDTO;
import com.phrmSystem.phrmSystem.dto.UserDTO;

import java.util.List;

public interface DoctorService {
    User createDoctor(User doctor);

    User updateDoctor(Long doctorId, User updatedDoctor);

    void deleteDoctor(Long doctorId);

    List<UserDTO> getAllDoctors();

    UserDTO getDoctorById(Long id);

    User getDoctorByUniqueId(String uniqueId);

    List<User> getAllPersonalDoctors();

    List<SpecializationDTO> getDoctorSpecializations(Long doctorId);

    List<AppointmentDTO> getDoctorAppointments(Long doctorId);

    UserDTO addSpecializationToDoctor(Long doctorId, SpecializationDTO specializationDTO);
}
