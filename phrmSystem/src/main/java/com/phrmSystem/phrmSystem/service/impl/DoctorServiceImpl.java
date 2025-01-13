package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.Role;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.entity.DoctorSpecialization;
import com.phrmSystem.phrmSystem.data.entity.DoctorAppointment;
import com.phrmSystem.phrmSystem.data.repo.DoctorRepository;
import com.phrmSystem.phrmSystem.data.repo.RoleRepository;
import com.phrmSystem.phrmSystem.dto.AppointmentDTO;
import com.phrmSystem.phrmSystem.dto.SpecializationDTO;
import com.phrmSystem.phrmSystem.dto.UserDTO;
import com.phrmSystem.phrmSystem.mappers.UserMapper;
import com.phrmSystem.phrmSystem.service.DoctorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final RoleRepository roleRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository, RoleRepository roleRepository) {
        this.doctorRepository = doctorRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User createDoctor(User doctor) {
        // Fetch the role from the database
        Role doctorRole = roleRepository.findByRoleName("DOCTOR")
                .orElseThrow(() -> new RuntimeException("Role DOCTOR not found"));

        // Assign the existing role to the doctor
        doctor.setRole(List.of(doctorRole));

        // Validate required fields for doctors
        if (doctor.getUniqueId() == null || doctor.getUniqueId().isEmpty()) {
            throw new RuntimeException("Unique ID is required for doctors.");
        }

        if (doctor.getIsPersonalDoctor() == null) {
            throw new RuntimeException("Personal doctor status must be specified for doctors.");
        }

        // Save the doctor to the database
        return doctorRepository.save(doctor);
    }



    @Override
    public User updateDoctor(Long doctorId, User updatedDoctor) {
        // Fetch the existing doctor
        User existingDoctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));

        // Validate the updated doctor fields
        if (updatedDoctor.getRole() == null || updatedDoctor.getRole().stream().noneMatch(role -> "DOCTOR".equals(role.getRoleName()))) {
            throw new RuntimeException("Updated user must have a role of DOCTOR.");
        }

        // Update fields
        existingDoctor.setFirstName(updatedDoctor.getFirstName());
        existingDoctor.setLastName(updatedDoctor.getLastName());
        existingDoctor.setUniqueId(updatedDoctor.getUniqueId());
        existingDoctor.setIsPersonalDoctor(updatedDoctor.getIsPersonalDoctor());

        // Save and return the updated doctor
        return doctorRepository.save(existingDoctor);
    }

    @Override
    public void deleteDoctor(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new RuntimeException("Doctor not found with ID: " + doctorId);
        }
        doctorRepository.deleteById(doctorId);
    }

    @Override
    public List<UserDTO> getAllDoctors() {
        // Fetch doctors and map them to DTOs
        return doctorRepository.findAllDoctors()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public UserDTO getDoctorById(Long id) {
        User doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        return UserMapper.toDTO(doctor);
    }

    @Override
    public User getDoctorByUniqueId(String uniqueId) {
        return doctorRepository.findDoctorByUniqueId(uniqueId);
    }

    @Override
    public List<User> getAllPersonalDoctors() {
        return doctorRepository.findAllPersonalDoctors();
    }

    @Override
    public List<SpecializationDTO> getDoctorSpecializations(Long doctorId) {
        User doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));

        return doctor.getSpecializations().stream()
                .map(specialization -> new SpecializationDTO(
                        specialization.getId(),
                        specialization.getSpecialization()
                ))
                .collect(Collectors.toList());
    }


    @Override
    public List<AppointmentDTO> getDoctorAppointments(Long doctorId) {
        User doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));

        return doctor.getDoctorAppointments().stream()
                .map(appointment -> new AppointmentDTO(
                        appointment.getId(),
                        appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName(),
                        appointment.getDate()
                ))
                .collect(Collectors.toList());
    }

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
}
