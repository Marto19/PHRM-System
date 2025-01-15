package com.phrmSystem.phrmSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisDTO {
    private Long id;
    private String diagnosisName;
    private String diagnosisDescription;
    private Set<Long> sickDayIds;
    private Long doctorAppointmentId;
    private Set<Long> medicineIds;
}
