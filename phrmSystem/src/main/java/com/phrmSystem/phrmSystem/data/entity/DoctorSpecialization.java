package com.phrmSystem.phrmSystem.data.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "doctor_specializations")
public class DoctorSpecialization extends BaseEntity {

    @Column(name = "specialization", nullable = false)
    private String specialization;

    @ManyToMany(mappedBy = "specializations")
    private Set<User> doctors = new HashSet<>();
}
