package com.phrmSystem.phrmSystem.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "diagnoses")
public class Diagnosis extends BaseEntity {

    @Column(name = "diagnosis_name", nullable = false)
    private String diagnosisName;

    @Column(name = "diagnosis_description")
    private String diagnosisDescription;

    @ManyToMany
    private Set<SickDay> sickDays;

    @ManyToOne
    private DoctorAppointment doctorAppointment;

    @OneToMany(mappedBy = "diagnosis")
    private Set<Medicine> medicine;

}
