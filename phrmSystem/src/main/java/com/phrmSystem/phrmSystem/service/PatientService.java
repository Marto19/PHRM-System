package com.phrmSystem.phrmSystem.service;

import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.entity.PatientIllnessHistory;
import com.phrmSystem.phrmSystem.data.entity.DoctorAppointment;
import com.phrmSystem.phrmSystem.dto.UserDTO;

import java.util.List;

public interface PatientService {
    User createPatient(User patient);
    User updatePatient(Long patientId, User updatedPatient);
    void deletePatient(Long patientId);
    List<UserDTO> getAllPatients();
    User getPatientById(Long id);
    User getPatientByUniqueIdentification(String uniqueIdentification);
    List<User> getPatientsWithInsurancePaid();
    List<PatientIllnessHistory> getPatientIllnessHistory(Long patientId);
    DoctorAppointment createAppointment(Long patientId, DoctorAppointment appointment);
}
