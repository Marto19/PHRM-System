package com.phrmSystem.phrmSystem.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "diagnosis")
public class Diagnosis extends BaseEntity {

    @Column(name = "diagnosis_name", nullable = false)
    private String diagnosisName;

    @Column(name = "diagnosis_description")
    private String diagnosisDescription;
}
