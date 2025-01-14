package com.phrmSystem.phrmSystem.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DoctorAppointmentAllDTO {
    private Long id;
    private LocalDateTime date;
    private Long patientId;
    private Long doctorId;
    private Long patientIllnessHistoryId;
    private Set<Long> diagnosisIds;
}
