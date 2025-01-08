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
@Table(name = "doctors")
public class Doctor extends BaseEntity {

    @Column(name = "unique_id", nullable = false, unique = true)
    private String uniqueId;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_personal_doctor", nullable = false)
    private boolean isPersonalDoctor;

    @ManyToMany
    @JoinTable(
            name = "doctor_specializations",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    private Set<DoctorSpecialization> specializations = new HashSet<>();
}
