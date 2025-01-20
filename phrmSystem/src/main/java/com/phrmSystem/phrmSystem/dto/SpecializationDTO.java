package com.phrmSystem.phrmSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class SpecializationDTO {
    private Long id;
    private String name;
    private String description;

    public SpecializationDTO(Long id, String specialization) {

    }
}
