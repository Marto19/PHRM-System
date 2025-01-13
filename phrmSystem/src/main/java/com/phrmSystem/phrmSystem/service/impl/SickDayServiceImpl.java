package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.Diagnosis;
import com.phrmSystem.phrmSystem.data.entity.SickDay;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.repo.DiagnosisRepository;
import com.phrmSystem.phrmSystem.data.repo.SickDayRepository;
import com.phrmSystem.phrmSystem.data.repo.UserRepository;
import com.phrmSystem.phrmSystem.dto.SickDayDTO;
import com.phrmSystem.phrmSystem.service.SickDayService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SickDayServiceImpl implements SickDayService {

    private final SickDayRepository sickDayRepository;
    private final UserRepository userRepository;
    private final DiagnosisRepository diagnosisRepository;

    public SickDayServiceImpl(SickDayRepository sickDayRepository, UserRepository userRepository, DiagnosisRepository diagnosisRepository) {
        this.sickDayRepository = sickDayRepository;
        this.userRepository = userRepository;
        this.diagnosisRepository = diagnosisRepository;
    }

    @Override
    public SickDayDTO getSickDayById(Long id) {
        SickDay sickDay = sickDayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SickDay not found with id: " + id));
        return mapToDTO(sickDay);
    }

    @Override
    public List<SickDayDTO> getAllSickDays() {
        return sickDayRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SickDayDTO createSickDay(SickDayDTO sickDayDTO) {
        SickDay sickDay = mapToEntity(sickDayDTO);
        SickDay savedSickDay = sickDayRepository.save(sickDay);
        return mapToDTO(savedSickDay);
    }

    @Override
    public SickDayDTO updateSickDay(Long id, SickDayDTO sickDayDTO) {
        SickDay sickDay = sickDayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SickDay not found with id: " + id));

        sickDay.setStartDate(sickDayDTO.getStartDate());
        sickDay.setEndDate(sickDayDTO.getEndDate());
        sickDay.setNumberOfDays(sickDayDTO.getNumberOfDays());

        if (sickDayDTO.getPatientId() != null) {
            User patient = userRepository.findById(sickDayDTO.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found with id: " + sickDayDTO.getPatientId()));
            sickDay.setPatient(patient);
        }

        if (sickDayDTO.getDoctorId() != null) {
            User doctor = userRepository.findById(sickDayDTO.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + sickDayDTO.getDoctorId()));
            sickDay.setDoctor(doctor);
        }

        if (sickDayDTO.getDiagnosisIds() != null && !sickDayDTO.getDiagnosisIds().isEmpty()) {
            Set<Diagnosis> diagnoses = sickDayDTO.getDiagnosisIds().stream()
                    .map(diagnosisId -> diagnosisRepository.findById(diagnosisId)
                            .orElseThrow(() -> new RuntimeException("Diagnosis not found with id: " + diagnosisId)))
                    .collect(Collectors.toSet());
            sickDay.setDiagnosis(diagnoses);
        }

        SickDay updatedSickDay = sickDayRepository.save(sickDay);
        return mapToDTO(updatedSickDay);
    }

    @Override
    public void deleteSickDay(Long id) {
        SickDay sickDay = sickDayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SickDay not found with id: " + id));
        sickDayRepository.delete(sickDay);
    }

    private SickDayDTO mapToDTO(SickDay sickDay) {
        return new SickDayDTO(
                sickDay.getId(),
                sickDay.getStartDate(),
                sickDay.getEndDate(),
                sickDay.getNumberOfDays(),
                sickDay.getPatient() != null ? sickDay.getPatient().getId() : null,
                sickDay.getDiagnosis() != null ? sickDay.getDiagnosis().stream()
                        .map(Diagnosis::getId)
                        .collect(Collectors.toSet()) : null,
                sickDay.getDoctor() != null ? sickDay.getDoctor().getId() : null
        );
    }

    private SickDay mapToEntity(SickDayDTO sickDayDTO) {
        SickDay sickDay = new SickDay();
        sickDay.setStartDate(sickDayDTO.getStartDate());
        sickDay.setEndDate(sickDayDTO.getEndDate());
        sickDay.setNumberOfDays(sickDayDTO.getNumberOfDays());

        if (sickDayDTO.getPatientId() != null) {
            User patient = userRepository.findById(sickDayDTO.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found with id: " + sickDayDTO.getPatientId()));
            sickDay.setPatient(patient);
        }

        if (sickDayDTO.getDoctorId() != null) {
            User doctor = userRepository.findById(sickDayDTO.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + sickDayDTO.getDoctorId()));
            sickDay.setDoctor(doctor);
        }

        if (sickDayDTO.getDiagnosisIds() != null && !sickDayDTO.getDiagnosisIds().isEmpty()) {
            Set<Diagnosis> diagnoses = sickDayDTO.getDiagnosisIds().stream()
                    .map(diagnosisId -> diagnosisRepository.findById(diagnosisId)
                            .orElseThrow(() -> new RuntimeException("Diagnosis not found with id: " + diagnosisId)))
                    .collect(Collectors.toSet());
            sickDay.setDiagnosis(diagnoses);
        }

        return sickDay;
    }
}
