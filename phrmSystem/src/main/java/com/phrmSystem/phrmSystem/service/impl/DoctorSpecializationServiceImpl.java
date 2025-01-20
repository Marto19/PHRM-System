package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.DoctorSpecialization;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.repo.DoctorSpecializationRepository;
import com.phrmSystem.phrmSystem.data.repo.UserRepository;
import com.phrmSystem.phrmSystem.dto.DoctorSpecializationDTO;
import com.phrmSystem.phrmSystem.service.DoctorSpecializationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of DoctorSpecializationService for managing doctor specializations.
 */
@Service
public class DoctorSpecializationServiceImpl implements DoctorSpecializationService {

    private final DoctorSpecializationRepository doctorSpecializationRepository;
    private final UserRepository userRepository;

    public DoctorSpecializationServiceImpl(DoctorSpecializationRepository doctorSpecializationRepository, UserRepository userRepository) {
        this.doctorSpecializationRepository = doctorSpecializationRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a doctor specialization by ID.
     *
     * @param id the ID of the specialization.
     * @return the DoctorSpecializationDTO representation.
     */
    @Override
    public DoctorSpecializationDTO getDoctorSpecializationById(Long id) {
        DoctorSpecialization specialization = doctorSpecializationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor specialization not found with id: " + id));
        return mapToDTO(specialization);
    }

    /**
     * Retrieves all doctor specializations.
     *
     * @return a list of DoctorSpecializationDTOs.
     */
    @Override
    public List<DoctorSpecializationDTO> getAllDoctorSpecializations() {
        return doctorSpecializationRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new doctor specialization.
     *
     * @param dto the DTO containing specialization details.
     * @return the created DoctorSpecializationDTO.
     */
    @Override
    @Transactional
    public DoctorSpecializationDTO createDoctorSpecialization(DoctorSpecializationDTO dto) {
        validateDoctorSpecializationDTO(dto);

        DoctorSpecialization specialization = new DoctorSpecialization();
        specialization.setSpecialization(dto.getSpecialization());

        if (dto.getDoctorIds() != null && !dto.getDoctorIds().isEmpty()) {
            Set<User> doctors = dto.getDoctorIds().stream()
                    .map(this::findDoctorById)
                    .collect(Collectors.toSet());
            specialization.setDoctors(doctors);
            doctors.forEach(doctor -> doctor.getSpecializations().add(specialization));
        }

        return mapToDTO(doctorSpecializationRepository.save(specialization));
    }

    /**
     * Updates an existing doctor specialization by ID.
     *
     * @param id  the ID of the specialization to update.
     * @param dto the updated specialization details.
     * @return the updated DoctorSpecializationDTO.
     */
    @Override
    @Transactional
    public DoctorSpecializationDTO updateDoctorSpecialization(Long id, DoctorSpecializationDTO dto) {
        validateDoctorSpecializationDTO(dto);

        DoctorSpecialization specialization = doctorSpecializationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor specialization not found with id: " + id));

        specialization.setSpecialization(dto.getSpecialization());

        if (dto.getDoctorIds() != null) {
            Set<User> doctors = dto.getDoctorIds().stream()
                    .map(this::findDoctorById)
                    .collect(Collectors.toSet());
            specialization.setDoctors(doctors);
            doctors.forEach(doctor -> doctor.getSpecializations().add(specialization));
        }

        return mapToDTO(doctorSpecializationRepository.save(specialization));
    }

    /**
     * Deletes a doctor specialization by ID.
     *
     * @param id the ID of the specialization to delete.
     */
    @Override
    @Transactional
    public void deleteDoctorSpecialization(Long id) {
        DoctorSpecialization specialization = doctorSpecializationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor specialization not found with id: " + id));

        if (!specialization.getDoctors().isEmpty()) {
            throw new RuntimeException("Cannot delete specialization as it is assigned to doctors.");
        }

        doctorSpecializationRepository.delete(specialization);
    }

    /**
     * Validates a DoctorSpecializationDTO for required fields.
     *
     * @param dto the DTO to validate.
     */
    private void validateDoctorSpecializationDTO(DoctorSpecializationDTO dto) {
        if (dto.getSpecialization() == null || dto.getSpecialization().isBlank()) {
            throw new RuntimeException("Specialization name cannot be null or blank.");
        }
    }

    /**
     * Finds a doctor by ID.
     *
     * @param doctorId the ID of the doctor to find.
     * @return the User entity.
     */
    private User findDoctorById(Long doctorId) {
        return userRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));
    }

    /**
     * Maps a DoctorSpecialization entity to its DTO representation.
     *
     * @param specialization the DoctorSpecialization entity.
     * @return the corresponding DoctorSpecializationDTO.
     */
    private DoctorSpecializationDTO mapToDTO(DoctorSpecialization specialization) {
        return new DoctorSpecializationDTO(
                specialization.getId(),
                specialization.getSpecialization(),
                specialization.getDoctors()
                        .stream()
                        .map(User::getId)
                        .collect(Collectors.toSet())
        );
    }
}
