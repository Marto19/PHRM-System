package com.phrmSystem.phrmSystem.service;

import com.phrmSystem.phrmSystem.dto.DoctorAppointmentAllDTO;

import java.util.List;

public interface DoctorAppointmentService {
    DoctorAppointmentAllDTO createDoctorAppointment(DoctorAppointmentAllDTO appointmentDTO);

    DoctorAppointmentAllDTO updateDoctorAppointment(Long id, DoctorAppointmentAllDTO doctorAppointmentAllDTO);

    DoctorAppointmentAllDTO getDoctorAppointmentById(Long id);

    List<DoctorAppointmentAllDTO> getAllDoctorAppointments();

    void deleteDoctorAppointment(Long id);
}
