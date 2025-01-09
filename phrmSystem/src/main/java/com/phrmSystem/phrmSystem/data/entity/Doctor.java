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

    @Column(name = "is_personal_doctor", nullable = false)
    private boolean isPersonalDoctor;

    @OneToMany(mappedBy = "personalDoctor")
    private Set<Patient> patients;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "doctor")
    private Set<SickDay> sickDays;

    @OneToMany(mappedBy = "doctor")
    private Set<DoctorAppointment> doctorAppointments;

    @ManyToMany
//    @JoinTable(
//            name = "doctor_specializations",
//            joinColumns = @JoinColumn(name = "doctor_id"),
//            inverseJoinColumns = @JoinColumn(name = "specialization_id")
//    )
    private Set<DoctorSpecialization> specializations = new HashSet<>();
}
