package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.Doctor;
import com.phrmSystem.phrmSystem.data.entity.DoctorSpecialization;
import com.phrmSystem.phrmSystem.data.repo.DoctorRepository;
import com.phrmSystem.phrmSystem.data.repo.DoctorSpecializationRepository;
import com.phrmSystem.phrmSystem.service.DoctorService;
import com.phrmSystem.phrmSystem.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorSpecializationRepository specializationRepository;

//    private void validateDoctor(Doctor doctor) {
//        if (!StringUtils.hasText(doctor.getUniqueId())) {
//            throw new IllegalArgumentException("Doctor's unique ID cannot be blank.");
//        }
//        if (!StringUtils.hasText(doctor.getName())) {
//            throw new IllegalArgumentException("Doctor's name cannot be blank.");
//        }
//        if (doctor.getSpecializations() == null || doctor.getSpecializations().isEmpty()) {
//            throw new IllegalArgumentException("Doctor must have at least one specialization.");
//        }
//        // Ensure each specialization exists or is valid
//        for (DoctorSpecialization specialization : doctor.getSpecializations()) {
//            if (!StringUtils.hasText(specialization.getSpecialization())) {
//                throw new IllegalArgumentException("Specialization name cannot be blank.");
//            }
//        }
//        if (doctorRepository.findByUniqueId(doctor.getUniqueId()).isPresent()) {
//            throw new IllegalStateException("A doctor with this unique ID already exists.");
//        }
//    }


//    private void validateUpdatedDoctor(Doctor existingDoctor, Doctor updatedDoctor) {
//        if (!StringUtils.hasText(updatedDoctor.getName())) {
//            throw new IllegalArgumentException("Doctor's name cannot be blank.");
//        }
//        if (updatedDoctor.getSpecializations() == null || updatedDoctor.getSpecializations().isEmpty()) {
//            throw new IllegalArgumentException("Doctor must have at least one specialization.");
//        }
//    }


    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Doctor not found"));
    }

    @Override
    public Doctor createDoctor(Doctor doctor) {
        doctor.setUniqueId(generateUniqueId());
//        validateDoctor(doctor);

        // Persist specializations separately if needed
        for (DoctorSpecialization specialization : doctor.getSpecializations()) {
            if (specialization.getId() == 0) {
                // Persist new specializations
                specializationRepository.save(specialization);
            }
        }

        return doctorRepository.save(doctor);
    }


    private String generateUniqueId() {
        // Generate a unique identifier with a prefix (e.g., "DOC") and a random numeric part
        String prefix = "DOC";
        String uniqueNumber = String.valueOf(System.currentTimeMillis() % 1_000_000); // Timestamp-based unique number
        return prefix + uniqueNumber;
    }


    @Override
    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        Doctor existingDoctor = getDoctorById(id);
        existingDoctor.setUniqueId(updatedDoctor.getUniqueId());
        existingDoctor.setPersonalDoctor(updatedDoctor.isPersonalDoctor());
        existingDoctor.setSpecializations(updatedDoctor.getSpecializations());
        return doctorRepository.save(existingDoctor);
    }


    @Override
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Doctor with id " + id + " not found.");
        }
        doctorRepository.deleteById(id);
    }

    @Override
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        if (!StringUtils.hasText(specialization)) {
            throw new IllegalArgumentException("Specialization cannot be blank.");
        }
        return doctorRepository.findBySpecialization(specialization);
    }

    public int countPatientsByDoctorId(Long doctorId) {
        return doctorRepository.countPatientsByDoctorId(doctorId);
    }

    @Override
    public List<Doctor> getPersonalDoctors() {
        return doctorRepository.findByIsPersonalDoctor(true);
    }

    @Override
    public List<Object[]> getDoctorsWithMostPatients() {
        return doctorRepository.findAll() // Custom logic for most patients
                .stream()
                .map(doctor -> new Object[]{doctor, countPatientsByDoctorId(doctor.getId())})
                .sorted((o1, o2) -> Integer.compare((int) o2[1], (int) o1[1]))
                .toList();
    }
}
