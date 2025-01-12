package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.Role;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.entity.DoctorSpecialization;
import com.phrmSystem.phrmSystem.data.entity.DoctorAppointment;
import com.phrmSystem.phrmSystem.data.repo.DoctorRepository;
import com.phrmSystem.phrmSystem.data.repo.RoleRepository;
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
    public User getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
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
    public List<DoctorSpecialization> getDoctorSpecializations(Long doctorId) {
        User doctor = getDoctorById(doctorId);
        return List.copyOf(doctor.getSpecializations());
    }

    @Override
    public List<DoctorAppointment> getDoctorAppointments(Long doctorId) {
        User doctor = getDoctorById(doctorId);
        return List.copyOf(doctor.getDoctorAppointments());
    }

    @Override
    public User addSpecializationToDoctor(Long doctorId, DoctorSpecialization specialization) {
        User doctor = getDoctorById(doctorId);
        doctor.getSpecializations().add(specialization);
        return doctorRepository.save(doctor);
    }
}
