package com.phrmSystem.phrmSystem.service;

import com.phrmSystem.phrmSystem.data.entity.Doctor;

import java.util.List;

public interface DoctorService {
    List<Doctor> getAllDoctors();
    Doctor getDoctorById(Long id);
    Doctor createDoctor(Doctor doctor);
    Doctor updateDoctor(Long id, Doctor updatedDoctor);
    void deleteDoctor(Long id);
    List<Doctor> getDoctorsBySpecialization(String specialization);
    int countPatientsByDoctorId(Long doctorId);
    List<Object[]> getDoctorsWithMostPatients();
    List<Doctor> getPersonalDoctors();
}
