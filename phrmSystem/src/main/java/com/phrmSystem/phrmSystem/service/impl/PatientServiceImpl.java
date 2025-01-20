package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.Role;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.entity.PatientIllnessHistory;
import com.phrmSystem.phrmSystem.data.entity.DoctorAppointment;
import com.phrmSystem.phrmSystem.data.repo.PatientRepository;
import com.phrmSystem.phrmSystem.data.repo.DoctorAppointmentRepository;
import com.phrmSystem.phrmSystem.data.repo.RoleRepository;
import com.phrmSystem.phrmSystem.data.repo.UserRepository;
import com.phrmSystem.phrmSystem.dto.DoctorAppointmentDTO;
import com.phrmSystem.phrmSystem.dto.PatientIllnessHistoryDTO;
import com.phrmSystem.phrmSystem.dto.UserDTO;
import com.phrmSystem.phrmSystem.mappers.UserMapper;
import com.phrmSystem.phrmSystem.service.PatientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of PatientService interface, responsible for managing patient-related operations.
 */
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

    /**
     * Creates a new patient after validating the input and ensuring the PATIENT role is assigned.
     *
     * @param patient the User entity representing the patient.
     * @return the created User entity.
     */
    @Override
    @Transactional
    public User createPatient(User patient) {
        validatePatient(patient);

        Set<Role> roles = patient.getRole().stream()
                .map(role -> roleRepository.findByRoleName(role.getRoleName())
                        .orElseThrow(() -> new RuntimeException("Role not found: " + role.getRoleName())))
                .collect(Collectors.toSet());

        patient.setRole(new LinkedList<>(roles));
        return userRepository.save(patient);
    }

    /**
     * Updates an existing patient with the specified details.
     *
     * @param patientId      the ID of the patient to update.
     * @param updatedPatient the updated patient details.
     * @return the updated User entity.
     */
    @Override
    @Transactional
    public User updatePatient(Long patientId, User updatedPatient) {
        User existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));

        validatePatient(updatedPatient);

        existingPatient.setFirstName(updatedPatient.getFirstName());
        existingPatient.setLastName(updatedPatient.getLastName());
        existingPatient.setUniqueIdentification(updatedPatient.getUniqueIdentification());
        existingPatient.setInsurancePaidLast6Months(updatedPatient.getInsurancePaidLast6Months());

        return patientRepository.save(existingPatient);
    }

    /**
     * Deletes a patient by their ID, ensuring no active dependencies exist.
     *
     * @param patientId the ID of the patient to delete.
     */
    @Override
    @Transactional  //todo: make it gracefully
    public void deletePatient(Long patientId) {
        User patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));

        // Check for active doctor appointments and remove them
        if (!patient.getDoctorAppointment().isEmpty()) {
            throw new RuntimeException("Cannot delete patient with active doctor appointments. Please cancel or reassign the appointments first.");
        }

        // Ensure no remaining dependencies
        if (!patient.getPatientIllnessHistory().isEmpty()) {
            throw new RuntimeException("Cannot delete patient with active illness histories. Please delete or reassign the histories first.");
        }

        // Attempt to delete the patient
        try {
            patientRepository.deleteById(patientId);
        } catch (Exception ex) {
            if (ex.getMessage().contains("constraint")) {
                throw new RuntimeException("Cannot delete patient due to active dependencies. Ensure all references are removed.");
            }
            throw ex;
        }
    }




    /**
     * Retrieves all patients with the PATIENT role.
     *
     * @return a list of UserDTOs representing the patients.
     */
    @Override
    public List<UserDTO> getAllPatients() {
        List<User> users = patientRepository.findAllUsersByRoleName("PATIENT");
        return users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a patient by their ID.
     *
     * @param id the ID of the patient to retrieve.
     * @return the UserDTO representing the patient.
     */
    @Override
    public UserDTO getPatientById(Long id) {
        User patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        return UserMapper.toDTO(patient);
    }

    /**
     * Retrieves a patient by their unique identification.
     *
     * @param uniqueIdentification the unique identification of the patient.
     * @return the User entity representing the patient.
     */
    @Override
    public User getPatientByUniqueIdentification(String uniqueIdentification) {
        return patientRepository.findPatientByUniqueIdentification(uniqueIdentification);
    }

    /**
     * Retrieves all patients with their insurance paid within the last 6 months.
     *
     * @return a list of User entities representing the patients.
     */
    @Override
    public List<User> getPatientsWithInsurancePaid() {
        return patientRepository.findPatientsWithInsurancePaid();
    }

    /**
     * Retrieves the illness history of a patient by their ID.
     *
     * @param patientId the ID of the patient.
     * @return a list of PatientIllnessHistoryDTOs representing the patient's illness history.
     */
    @Override
    public List<PatientIllnessHistoryDTO> getPatientIllnessHistory(Long patientId) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        return patient.getPatientIllnessHistory().stream()
                .map(history -> new PatientIllnessHistoryDTO(
                        history.getId(),
                        history.getIllnessName(),
                        history.getStartDate(),
                        history.getEndDate(),
                        history.getPatient() != null ? history.getPatient().getId() : null
                ))
                .collect(Collectors.toList());
    }


    /**
     * Creates a new doctor appointment for a patient.
     *
     * @param patientId      the ID of the patient.
     * @param appointmentDTO the details of the appointment.
     * @return the created DoctorAppointmentDTO.
     */
    @Override
    @Transactional
    public DoctorAppointmentDTO createAppointment(Long patientId, DoctorAppointmentDTO appointmentDTO) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        DoctorAppointment appointment = new DoctorAppointment();
        appointment.setDate(appointmentDTO.getDate());
        appointment.setPatient(patient);

        DoctorAppointment savedAppointment = doctorAppointmentRepository.save(appointment);

        return new DoctorAppointmentDTO(
                savedAppointment.getId(),
                savedAppointment.getDate(),
                savedAppointment.getPatient().getId(),
                savedAppointment.getDoctor() != null ? savedAppointment.getDoctor().getId() : null
        );
    }

    /**
     * Validates a patient entity to ensure required fields are present and valid.
     *
     * @param patient the User entity representing the patient.
     * @throws RuntimeException if validation fails.
     */
    private void validatePatient(User patient) {
        if (patient.getRole() == null || patient.getRole().stream().noneMatch(role -> "PATIENT".equals(role.getRoleName()))) {
            throw new RuntimeException("User must have a role of PATIENT.");
        }
        if (patient.getUniqueIdentification() == null || patient.getUniqueIdentification().isEmpty()) {
            throw new RuntimeException("Unique identification is required for patients.");
        }
        userRepository.findByUniqueIdentification(patient.getUniqueIdentification())
                .ifPresent(existingPatient -> {
                    if (!existingPatient.getId().equals(patient.getId())) {
                        throw new RuntimeException("A patient with the unique identification '" + patient.getUniqueIdentification() + "' already exists.");
                    }
                });
    }
}
