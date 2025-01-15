package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.*;
import com.phrmSystem.phrmSystem.data.repo.*;
import com.phrmSystem.phrmSystem.dto.DiagnosisDTO;
import com.phrmSystem.phrmSystem.service.DiagnosisService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DiagnosisServiceImpl implements DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final SickDayRepository sickDayRepository;
    private final DoctorAppointmentRepository doctorAppointmentRepository;
    private final MedicineRepository medicineRepository;

    public DiagnosisServiceImpl(DiagnosisRepository diagnosisRepository,
                                SickDayRepository sickDayRepository,
                                DoctorAppointmentRepository doctorAppointmentRepository,
                                MedicineRepository medicineRepository) {
        this.diagnosisRepository = diagnosisRepository;
        this.sickDayRepository = sickDayRepository;
        this.doctorAppointmentRepository = doctorAppointmentRepository;
        this.medicineRepository = medicineRepository;
    }

    @Override
    public DiagnosisDTO createDiagnosis(DiagnosisDTO dto) {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setDiagnosisName(dto.getDiagnosisName());
        diagnosis.setDiagnosisDescription(dto.getDiagnosisDescription());

        if (dto.getSickDayIds() != null) {
            Set<SickDay> sickDays = dto.getSickDayIds().stream()
                    .map(id -> sickDayRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Sick Day not found with ID: " + id)))
                    .collect(Collectors.toSet());
            diagnosis.setSickDays(sickDays);
        }

        if (dto.getDoctorAppointmentId() != null) {
            DoctorAppointment doctorAppointment = doctorAppointmentRepository.findById(dto.getDoctorAppointmentId())
                    .orElseThrow(() -> new RuntimeException("Doctor Appointment not found with ID: " + dto.getDoctorAppointmentId()));
            diagnosis.setDoctorAppointment(doctorAppointment);
        }

        if (dto.getMedicineIds() != null) {
            Set<Medicine> medicines = dto.getMedicineIds().stream()
                    .map(id -> medicineRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Medicine not found with ID: " + id)))
                    .collect(Collectors.toSet());
            diagnosis.setMedicine(medicines);
        }

        Diagnosis savedDiagnosis = diagnosisRepository.save(diagnosis);
        return mapToDTO(savedDiagnosis);
    }

    @Override
    public DiagnosisDTO updateDiagnosis(Long id, DiagnosisDTO dto) {
        Diagnosis diagnosis = diagnosisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Diagnosis not found with ID: " + id));

        diagnosis.setDiagnosisName(dto.getDiagnosisName());
        diagnosis.setDiagnosisDescription(dto.getDiagnosisDescription());

        if (dto.getSickDayIds() != null) {
            Set<SickDay> sickDays = dto.getSickDayIds().stream()
                    .map(sickDayId -> sickDayRepository.findById(sickDayId)
                            .orElseThrow(() -> new RuntimeException("Sick Day not found with ID: " + sickDayId)))
                    .collect(Collectors.toSet());
            diagnosis.setSickDays(sickDays);
        }

        if (dto.getDoctorAppointmentId() != null) {
            DoctorAppointment doctorAppointment = doctorAppointmentRepository.findById(dto.getDoctorAppointmentId())
                    .orElseThrow(() -> new RuntimeException("Doctor Appointment not found with ID: " + dto.getDoctorAppointmentId()));
            diagnosis.setDoctorAppointment(doctorAppointment);
        }

        if (dto.getMedicineIds() != null) {
            Set<Medicine> medicines = dto.getMedicineIds().stream()
                    .map(medicineId -> medicineRepository.findById(medicineId)
                            .orElseThrow(() -> new RuntimeException("Medicine not found with ID: " + medicineId)))
                    .collect(Collectors.toSet());
            diagnosis.setMedicine(medicines);
        }

        Diagnosis updatedDiagnosis = diagnosisRepository.save(diagnosis);
        return mapToDTO(updatedDiagnosis);
    }

    @Override
    public void deleteDiagnosis(Long id) {
        diagnosisRepository.deleteById(id);
    }

    @Override
    public List<DiagnosisDTO> getAllDiagnoses() {
        return diagnosisRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DiagnosisDTO getDiagnosisById(Long id) {
        Diagnosis diagnosis = diagnosisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Diagnosis not found with ID: " + id));
        return mapToDTO(diagnosis);
    }

    private DiagnosisDTO mapToDTO(Diagnosis diagnosis) {
        DiagnosisDTO dto = new DiagnosisDTO();
        dto.setId(diagnosis.getId());
        dto.setDiagnosisName(diagnosis.getDiagnosisName());
        dto.setDiagnosisDescription(diagnosis.getDiagnosisDescription());

        if (diagnosis.getSickDays() != null) {
            dto.setSickDayIds(diagnosis.getSickDays().stream()
                    .map(SickDay::getId)
                    .collect(Collectors.toSet()));
        }

        if (diagnosis.getDoctorAppointment() != null) {
            dto.setDoctorAppointmentId(diagnosis.getDoctorAppointment().getId());
        }

        if (diagnosis.getMedicine() != null) {
            dto.setMedicineIds(diagnosis.getMedicine().stream()
                    .map(Medicine::getId)
                    .collect(Collectors.toSet()));
        }

        return dto;
    }
}
