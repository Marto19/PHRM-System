package com.phrmSystem.phrmSystem.service;

import com.phrmSystem.phrmSystem.data.entity.Doctor;

import java.util.List;

public interface DoctorService {

    List<Doctor> getAllDoctors();

    Doctor getDoctorById(long id);

    Doctor createDoctor(Doctor doctor);

    Doctor updateDoctor(long id, Doctor updatedDoctor);

    void deleteDoctor(long id);

    List<Doctor> findDoctorsBySpecialization(String specialization);

    List<Doctor> getPersonalDoctors();
}
