package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.Role;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.entity.PatientIllnessHistory;
import com.phrmSystem.phrmSystem.data.entity.DoctorAppointment;
import com.phrmSystem.phrmSystem.data.repo.PatientRepository;
import com.phrmSystem.phrmSystem.data.repo.DoctorAppointmentRepository;
import com.phrmSystem.phrmSystem.data.repo.RoleRepository;
import com.phrmSystem.phrmSystem.data.repo.UserRepository;
import com.phrmSystem.phrmSystem.dto.UserDTO;
import com.phrmSystem.phrmSystem.mappers.UserMapper;
import com.phrmSystem.phrmSystem.service.PatientService;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final DoctorAppointmentRepository doctorAppointmentRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public PatientServiceImpl(PatientRepository patientRepository, DoctorAppointmentRepository doctorAppointmentRepository, RoleRepository roleRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.doctorAppointmentRepository = doctorAppointmentRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    //    @Override
//    public User createPatient(User patient) {
//        // Validate that the role is PATIENT
//        if (patient.getRole() == null || patient.getRole().stream().noneMatch(role -> "PATIENT".equals(role.getRoleName()))) {
//            throw new RuntimeException("User must have a role of PATIENT.");
//        }
//
//        // Validate that uniqueIdentification is provided
//        if (patient.getUniqueIdentification() == null || patient.getUniqueIdentification().isEmpty()) {
//            throw new RuntimeException("Unique identification is required for patients.");
//        }
//
//        // Save the patient to the database
//        return patientRepository.save(patient);
//    }
@Override
public User createPatient(User patient) {
    // Fetch existing roles
    Set<Role> roles = patient.getRole().stream()
            .map(role -> roleRepository.findByRoleName(role.getRoleName())
                    .orElseThrow(() -> new RuntimeException("Role not found: " + role.getRoleName())))
            .collect(Collectors.toSet());

    // Set roles on the patient
    patient.setRole(new LinkedList<>(roles));

    // Save the patient
    return userRepository.save(patient);
}


    @Override
    public User updatePatient(Long patientId, User updatedPatient) {
        // Fetch the existing patient
        User existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));

        // Validate the updated patient fields
        if (updatedPatient.getRole() == null || updatedPatient.getRole().stream().noneMatch(role -> "PATIENT".equals(role.getRoleName()))) {
            throw new RuntimeException("Updated user must have a role of PATIENT.");
        }

        // Update fields
        existingPatient.setFirstName(updatedPatient.getFirstName());
        existingPatient.setLastName(updatedPatient.getLastName());
        existingPatient.setUniqueIdentification(updatedPatient.getUniqueIdentification());
        existingPatient.setInsurancePaidLast6Months(updatedPatient.getInsurancePaidLast6Months());

        // Save and return the updated patient
        return patientRepository.save(existingPatient);
    }

    @Override
    public void deletePatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new RuntimeException("Patient not found with ID: " + patientId);
        }
        patientRepository.deleteById(patientId);
    }

    @Override
    public List<UserDTO> getAllPatients() {
        List<User> users = patientRepository.findAllUsersByRoleName("PATIENT");
        return users.stream()
                .map(UserMapper::toDTO) // Assuming UserMapper handles role transformation
                .collect(Collectors.toList());
    }


    @Override
    public User getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
    }

    @Override
    public User getPatientByUniqueIdentification(String uniqueIdentification) {
        return patientRepository.findPatientByUniqueIdentification(uniqueIdentification);
    }

    @Override
    public List<User> getPatientsWithInsurancePaid() {
        return patientRepository.findPatientsWithInsurancePaid();
    }

    @Override
    public List<PatientIllnessHistory> getPatientIllnessHistory(Long patientId) {
        User patient = getPatientById(patientId);
        return List.copyOf(patient.getPatientIllnessHistory());
    }

    @Override
    public DoctorAppointment createAppointment(Long patientId, DoctorAppointment appointment) {
        User patient = getPatientById(patientId);
        appointment.setPatient(patient);
        return doctorAppointmentRepository.save(appointment);
    }
}
