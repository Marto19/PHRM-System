package com.phrmSystem.phrmSystem.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MedicineDTO {
    private Long id;
    private String medicineName;
    private String medicineDescription;
    private Long diagnosisId;
}
