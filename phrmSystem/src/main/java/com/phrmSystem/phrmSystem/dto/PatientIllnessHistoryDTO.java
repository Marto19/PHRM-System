package com.phrmSystem.phrmSystem.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatientIllnessHistoryDTO {
    private Long id;
    private String illnessName;
    private LocalDate startDate;
    private LocalDate endDate;
}
