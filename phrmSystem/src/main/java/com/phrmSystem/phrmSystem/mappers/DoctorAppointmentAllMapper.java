package com.phrmSystem.phrmSystem.mappers;

import com.phrmSystem.phrmSystem.data.entity.DoctorAppointment;
import com.phrmSystem.phrmSystem.dto.DoctorAppointmentAllDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DoctorAppointmentAllMapper {

    public static DoctorAppointmentAllDTO toDTO(DoctorAppointment entity) {
        DoctorAppointmentAllDTO dto = new DoctorAppointmentAllDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());

        if (entity.getPatient() != null) {
            dto.setPatientId(entity.getPatient().getId());
        }

        if (entity.getDoctor() != null) {
            dto.setDoctorId(entity.getDoctor().getId());
        }

        if (entity.getPatientIllnessHistory() != null) {
            dto.setPatientIllnessHistoryId(entity.getPatientIllnessHistory().getId());
        }

        if (entity.getDiagnosis() != null && !entity.getDiagnosis().isEmpty()) {
            dto.setDiagnosisIds(entity.getDiagnosis().stream()
                    .map(diagnosis -> diagnosis.getId())
                    .collect(Collectors.toSet()));
        }

        return dto;
    }

    public static DoctorAppointment toEntity(DoctorAppointmentAllDTO dto) {
        DoctorAppointment entity = new DoctorAppointment();
        entity.setId(dto.getId());
        entity.setDate(dto.getDate());
        // Relationships like patient, doctor, etc., are not directly mapped here.
        return entity;
    }
}
