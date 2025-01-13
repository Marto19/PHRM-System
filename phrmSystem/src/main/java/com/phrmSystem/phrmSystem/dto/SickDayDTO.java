package com.phrmSystem.phrmSystem.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SickDayDTO {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfDays;
    private Long patientId;
    private Set<Long> diagnosisIds;
    private Long doctorId;
}
