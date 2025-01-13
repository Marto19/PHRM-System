package com.phrmSystem.phrmSystem.service;

import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.entity.PatientIllnessHistory;
import com.phrmSystem.phrmSystem.data.entity.DoctorAppointment;
import com.phrmSystem.phrmSystem.dto.DoctorAppointmentDTO;
import com.phrmSystem.phrmSystem.dto.PatientIllnessHistoryDTO;
import com.phrmSystem.phrmSystem.dto.UserDTO;

import java.util.List;

public interface PatientService {
    User createPatient(User patient);
    User updatePatient(Long patientId, User updatedPatient);
    void deletePatient(Long patientId);
    List<UserDTO> getAllPatients();
    UserDTO getPatientById(Long id);
    User getPatientByUniqueIdentification(String uniqueIdentification);
    List<User> getPatientsWithInsurancePaid();
    List<PatientIllnessHistoryDTO> getPatientIllnessHistory(Long patientId);
    public DoctorAppointmentDTO createAppointment(Long patientId, DoctorAppointmentDTO appointmentDTO);
}
