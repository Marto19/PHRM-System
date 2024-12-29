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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "doctor_specializations", joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "specialization")
    private Set<String> specializations = new HashSet<>();

    @Column(name = "is_personal_doctor", nullable = false)
    private boolean isPersonalDoctor;
}
