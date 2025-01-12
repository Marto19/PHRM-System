package com.phrmSystem.phrmSystem.service;

import com.phrmSystem.phrmSystem.data.entity.Patient;

import java.util.List;

public interface PatientService {

    Patient createPatient(Patient patient);

    Patient updatePatient(Long patientId, Patient updatedPatient);

    void deletePatient(Long patientId);

    Patient getPatientById(Long patientId);

    List<Patient> getAllPatients();

    Patient findByUniqueIdentification(String uniqueIdentification);
}
