package com.phrmSystem.phrmSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecializationDTO {
    private Long id;
    private String name;
    private String description;

    public SpecializationDTO(Long id, String specialization) {

    }
}
