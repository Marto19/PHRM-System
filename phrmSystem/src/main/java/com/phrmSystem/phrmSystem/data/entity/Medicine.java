package com.phrmSystem.phrmSystem.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "medicines")
public class Medicine extends BaseEntity {

    @Column(name = "medicine_name", nullable = false)
    private String medicineName;

    @Column(name = "medicine_description")
    private String medicineDescription;

    @ManyToOne
    @JoinColumn(name = "diagnosis_id", nullable = false)
    private Diagnosis diagnosis;
}
