package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.DoctorSpecialization;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.repo.DoctorSpecializationRepository;
import com.phrmSystem.phrmSystem.data.repo.UserRepository;
import com.phrmSystem.phrmSystem.dto.DoctorSpecializationDTO;
import com.phrmSystem.phrmSystem.service.DoctorSpecializationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DoctorSpecializationServiceImpl implements DoctorSpecializationService {

    private final DoctorSpecializationRepository doctorSpecializationRepository;
    private final UserRepository userRepository;

    public DoctorSpecializationServiceImpl(DoctorSpecializationRepository doctorSpecializationRepository, UserRepository userRepository) {
        this.doctorSpecializationRepository = doctorSpecializationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DoctorSpecializationDTO getDoctorSpecializationById(Long id) {
        DoctorSpecialization specialization = doctorSpecializationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor specialization not found with id: " + id));
        return mapToDTO(specialization);
    }

    @Override
    public List<DoctorSpecializationDTO> getAllDoctorSpecializations() {
        return doctorSpecializationRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorSpecializationDTO createDoctorSpecialization(DoctorSpecializationDTO dto) {
        // Create a new specialization entity
        DoctorSpecialization specialization = new DoctorSpecialization();
        specialization.setSpecialization(dto.getSpecialization());

        // Save the specialization entity first
        DoctorSpecialization savedSpecialization = doctorSpecializationRepository.save(specialization);

        // Map doctor IDs to User entities and associate them with the specialization
        if (dto.getDoctorIds() != null && !dto.getDoctorIds().isEmpty()) {
            Set<User> doctors = dto.getDoctorIds().stream()
                    .map(id -> userRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id)))
                    .collect(Collectors.toSet());

            savedSpecialization.setDoctors(doctors);
            doctors.forEach(doctor -> doctor.getSpecializations().add(savedSpecialization));

            // Save the updated doctor entities to persist the relationship
            userRepository.saveAll(doctors);
        }

        // Return the DTO without using a mapper
        DoctorSpecializationDTO resultDto = new DoctorSpecializationDTO();
        resultDto.setId(savedSpecialization.getId());
        resultDto.setSpecialization(savedSpecialization.getSpecialization());
        resultDto.setDoctorIds(
                savedSpecialization.getDoctors().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet())
        );

        return resultDto;
    }


    @Override
    public DoctorSpecializationDTO updateDoctorSpecialization(Long id, DoctorSpecializationDTO doctorSpecializationDTO) {
        DoctorSpecialization specialization = doctorSpecializationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor specialization not found with id: " + id));

        specialization.setSpecialization(doctorSpecializationDTO.getSpecialization());

        if (doctorSpecializationDTO.getDoctorIds() != null) {
            Set<User> doctors = doctorSpecializationDTO.getDoctorIds()
                    .stream()
                    .map(doctorId -> userRepository.findById(doctorId)
                            .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId)))
                    .collect(Collectors.toSet());
            specialization.setDoctors(doctors);
        }

        return mapToDTO(doctorSpecializationRepository.save(specialization));
    }

    @Override
    public void deleteDoctorSpecialization(Long id) {
        DoctorSpecialization specialization = doctorSpecializationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor specialization not found with id: " + id));
        doctorSpecializationRepository.delete(specialization);
    }

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
