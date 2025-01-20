package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.Diagnosis;
import com.phrmSystem.phrmSystem.data.entity.DoctorAppointment;
import com.phrmSystem.phrmSystem.data.entity.PatientIllnessHistory;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.repo.DiagnosisRepository;
import com.phrmSystem.phrmSystem.data.repo.DoctorAppointmentRepository;
import com.phrmSystem.phrmSystem.data.repo.PatientIllnessHistoryRepository;
import com.phrmSystem.phrmSystem.data.repo.UserRepository;
import com.phrmSystem.phrmSystem.dto.DoctorAppointmentAllDTO;
import com.phrmSystem.phrmSystem.mappers.DoctorAppointmentAllMapper;
import com.phrmSystem.phrmSystem.service.DoctorAppointmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DoctorAppointmentServiceImpl implements DoctorAppointmentService {

    private final DoctorAppointmentRepository doctorAppointmentRepository;
    private final UserRepository userRepository;
    private final PatientIllnessHistoryRepository patientIllnessHistoryRepository;
    private final DiagnosisRepository diagnosisRepository;

    public DoctorAppointmentServiceImpl(DoctorAppointmentRepository doctorAppointmentRepository, UserRepository userRepository, PatientIllnessHistoryRepository patientIllnessHistoryRepository, DiagnosisRepository diagnosisRepository) {
        this.doctorAppointmentRepository = doctorAppointmentRepository;
        this.userRepository = userRepository;
        this.patientIllnessHistoryRepository = patientIllnessHistoryRepository;
        this.diagnosisRepository = diagnosisRepository;
    }

    @Override
    public DoctorAppointmentAllDTO createDoctorAppointment(DoctorAppointmentAllDTO appointmentDTO) {
        DoctorAppointment appointment = new DoctorAppointment();
        appointment.setDate(appointmentDTO.getDate());
        appointment.setPatient(userRepository.findById(appointmentDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found")));
        appointment.setDoctor(userRepository.findById(appointmentDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found")));

        DoctorAppointment savedAppointment = doctorAppointmentRepository.save(appointment);
        return mapToDTO(savedAppointment);
    }

    @Override
    public DoctorAppointmentAllDTO updateDoctorAppointment(Long id, DoctorAppointmentAllDTO doctorAppointmentAllDTO) {
        // Retrieve the appointment by ID
        DoctorAppointment appointment = doctorAppointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor Appointment not found with id: " + id));

        // Update the appointment fields
        appointment.setDate(doctorAppointmentAllDTO.getDate());

        // Update the patient
        if (doctorAppointmentAllDTO.getPatientId() != null) {
            User patient = userRepository.findById(doctorAppointmentAllDTO.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found with id: " + doctorAppointmentAllDTO.getPatientId()));
            appointment.setPatient(patient);
        }

        // Update the doctor
        if (doctorAppointmentAllDTO.getDoctorId() != null) {
            User doctor = userRepository.findById(doctorAppointmentAllDTO.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorAppointmentAllDTO.getDoctorId()));
            appointment.setDoctor(doctor);
        }

        // Update the patient illness history
        if (doctorAppointmentAllDTO.getPatientIllnessHistoryId() != null) {
            PatientIllnessHistory history = patientIllnessHistoryRepository.findById(doctorAppointmentAllDTO.getPatientIllnessHistoryId())
                    .orElseThrow(() -> new RuntimeException("Patient Illness History not found with id: " + doctorAppointmentAllDTO.getPatientIllnessHistoryId()));
            appointment.setPatientIllnessHistory(history);
        }

        // Update diagnoses
        if (doctorAppointmentAllDTO.getDiagnosisIds() != null && !doctorAppointmentAllDTO.getDiagnosisIds().isEmpty()) {
            Set<Diagnosis> diagnoses = doctorAppointmentAllDTO.getDiagnosisIds().stream()
                    .map(diagnosisId -> diagnosisRepository.findById(diagnosisId)
                            .orElseThrow(() -> new RuntimeException("Diagnosis not found with id: " + diagnosisId)))
                    .collect(Collectors.toSet());
            appointment.setDiagnosis(diagnoses);
        }

        // Save and return the updated entity as DTO
        DoctorAppointment updatedAppointment = doctorAppointmentRepository.save(appointment);
        return DoctorAppointmentAllMapper.toDTO(updatedAppointment);
    }

    @Override
    public DoctorAppointmentAllDTO getDoctorAppointmentById(Long id) {
        DoctorAppointment appointment = doctorAppointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        return mapToDTO(appointment);
    }

    @Override
    public List<DoctorAppointmentAllDTO> getAllDoctorAppointments() {
        return doctorAppointmentRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override   //todo: fix 400 bad request - make it gracefully
    public void deleteDoctorAppointment(Long id) {
        if (!doctorAppointmentRepository.existsById(id)) {
            throw new RuntimeException("Appointment not found");
        }
        doctorAppointmentRepository.deleteById(id);
    }

    private DoctorAppointmentAllDTO mapToDTO(DoctorAppointment appointment) {
        return new DoctorAppointmentAllDTO(
                appointment.getId(),
                appointment.getDate(),
                appointment.getPatient().getId(),
                appointment.getDoctor().getId(),
                appointment.getPatientIllnessHistory() != null ? appointment.getPatientIllnessHistory().getId() : null,
                appointment.getDiagnosis() != null ? appointment.getDiagnosis().stream().map(d -> d.getId()).collect(Collectors.toSet()) : null
        );
    }
}


//TODO: diagnosis are not being remembered when creating POST doctors Appointment record
//TODO: create the CRUD for the last entity - Diagnosis, postman, ect.
//TODO: make the WEB views of the pages.
//TODO: find a way to create user in DB when creating one in keycloak. The same with roles.
//TODO: implement better business logic. QUERIES FROM THE PDF!!!