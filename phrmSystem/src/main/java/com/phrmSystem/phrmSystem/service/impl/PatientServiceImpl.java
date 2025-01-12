package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.Patient;
import com.phrmSystem.phrmSystem.data.repo.PatientRepository;
import com.phrmSystem.phrmSystem.service.PatientService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(Long patientId, Patient updatedPatient) {
        Optional<Patient> existingPatient = patientRepository.findById(patientId);
        if (existingPatient.isPresent()) {
            Patient patient = existingPatient.get();
            patient.setUniqueIdentification(updatedPatient.getUniqueIdentification());
            patient.setInsurancePaidLast6Months(updatedPatient.isInsurancePaidLast6Months());
            patient.setUser(updatedPatient.getUser());
            patient.setPersonalDoctor(updatedPatient.getPersonalDoctor());
            return patientRepository.save(patient);
        }
        throw new RuntimeException("Patient not found with id: " + patientId);
    }

    @Override
    public void deletePatient(Long patientId) {
        if (patientRepository.existsById(patientId)) {
            patientRepository.deleteById(patientId);
        } else {
            throw new RuntimeException("Patient not found with id: " + patientId);
        }
    }

    @Override
    public Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId).orElseThrow(() ->
                new RuntimeException("Patient not found with id: " + patientId));
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient findByUniqueIdentification(String uniqueIdentification) {
        return patientRepository.findByUniqueIdentification(uniqueIdentification)
                .orElseThrow(() -> new RuntimeException("Patient not found with unique identification: " + uniqueIdentification));
    }

}
