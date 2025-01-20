package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.*;
import com.phrmSystem.phrmSystem.data.repo.DoctorRepository;
import com.phrmSystem.phrmSystem.data.repo.RoleRepository;
import com.phrmSystem.phrmSystem.dto.*;
import com.phrmSystem.phrmSystem.mappers.UserMapper;
import com.phrmSystem.phrmSystem.service.DoctorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of DoctorService, responsible for managing doctor-related operations.
 */
@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final RoleRepository roleRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository, RoleRepository roleRepository) {
        this.doctorRepository = doctorRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Creates a new doctor after validating the input.
     *
     * @param doctor the User entity representing the doctor.
     * @return the created User entity.
     */
    @Override
    public User createDoctor(User doctor) {
        validateDoctor(doctor);

        Role doctorRole = roleRepository.findByRoleName("DOCTOR")
                .orElseThrow(() -> new RuntimeException("Role DOCTOR not found."));

        doctor.setRole(List.of(doctorRole));
        return doctorRepository.save(doctor);
    }

    /**
     * Updates an existing doctor with new details.
     *
     * @param doctorId      the ID of the doctor to update.
     * @param updatedDoctor the updated doctor details.
     * @return the updated User entity.
     */
    @Override
    public User updateDoctor(Long doctorId, User updatedDoctor) {
        User existingDoctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));

        validateDoctor(updatedDoctor);

        existingDoctor.setFirstName(updatedDoctor.getFirstName());
        existingDoctor.setLastName(updatedDoctor.getLastName());
        existingDoctor.setUniqueId(updatedDoctor.getUniqueId());
        existingDoctor.setIsPersonalDoctor(updatedDoctor.getIsPersonalDoctor());

        return doctorRepository.save(existingDoctor);
    }

    /**
     * Deletes a doctor by ID, ensuring no active dependencies exist.
     *
     * @param doctorId the ID of the doctor to delete.
     */
    @Override
    @Transactional
    public void deleteDoctor(Long doctorId) {
        User doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));

        // Check for active appointments
        if (!doctor.getDoctorAppointments().isEmpty()) {
            throw new RuntimeException("Cannot delete doctor with active appointments. Please reassign or cancel appointments first.");
        }

        try {
            doctorRepository.deleteById(doctorId);
        } catch (Exception ex) {
            if (ex.getMessage().contains("constraint")) {
                throw new RuntimeException("Cannot delete doctor due to active dependencies. Ensure all references are removed.");
            }
            throw ex;
        }
    }


    /**
     * Retrieves all doctors.
     *
     * @return a list of UserDTOs representing doctors.
     */
    @Override
    public List<UserDTO> getAllDoctors() {
        return doctorRepository.findAllDoctors()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a doctor by ID.
     *
     * @param id the ID of the doctor to retrieve.
     * @return the UserDTO representation of the doctor.
     */
    @Override
    public UserDTO getDoctorById(Long id) {
        User doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        return UserMapper.toDTO(doctor);
    }

    /**
     * Retrieves a doctor by unique ID.
     *
     * @param uniqueId the unique ID of the doctor.
     * @return the User entity representing the doctor.
     */
    @Override
    public User getDoctorByUniqueId(String uniqueId) {
        return doctorRepository.findDoctorByUniqueId(uniqueId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with unique ID: " + uniqueId));
    }

    /**
     * Retrieves all personal doctors.
     *
     * @return a list of User entities representing personal doctors.
     */
    @Override
    public List<User> getAllPersonalDoctors() {
        return doctorRepository.findAllPersonalDoctors();
    }

    /**
     * Retrieves specializations for a doctor.
     *
     * @param doctorId the ID of the doctor.
     * @return a list of SpecializationDTOs.
     */
    @Override
    public List<SpecializationDTO> getDoctorSpecializations(Long doctorId) {
        User doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));

        return doctor.getSpecializations()
                .stream()
                .map(spec -> new SpecializationDTO(spec.getId(), spec.getSpecialization()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves appointments for a doctor.
     *
     * @param doctorId the ID of the doctor.
     * @return a list of AppointmentDTOs.
     */
    @Override
    public List<AppointmentDTO> getDoctorAppointments(Long doctorId) {
        User doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));

        return doctor.getDoctorAppointments()
                .stream()
                .map(appointment -> new AppointmentDTO(
                        appointment.getId(),
                        appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName(),
                        appointment.getDate()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Adds a specialization to a doctor.
     *
     * @param doctorId           the ID of the doctor.
     * @param specializationDTO  the specialization details.
     * @return the updated UserDTO.
     */
    @Override
    public UserDTO addSpecializationToDoctor(Long doctorId, SpecializationDTO specializationDTO) {
        User doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));

        DoctorSpecialization specialization = new DoctorSpecialization();
        specialization.setSpecialization(specializationDTO.getName());

        doctor.getSpecializations().add(specialization);

        User updatedDoctor = doctorRepository.save(doctor);
        return UserMapper.toDTO(updatedDoctor);
    }

    /**
     * Validates the required fields for a doctor.
     *
     * @param doctor the User entity representing the doctor.
     */
    private void validateDoctor(User doctor) {
        if (doctor.getUniqueId() == null || doctor.getUniqueId().isBlank()) {
            throw new RuntimeException("Unique ID is required for doctors.");
        }
        if (doctor.getIsPersonalDoctor() == null) {
            throw new RuntimeException("Personal doctor status must be specified.");
        }
    }
}
