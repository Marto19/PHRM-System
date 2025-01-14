package com.phrmSystem.phrmSystem.dto;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DoctorSpecializationDTO {
    private Long id;
    private String specialization;
    private Set<Long> doctorIds;
}
