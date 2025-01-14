package com.phrmSystem.phrmSystem.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ManyToMany
    @JoinTable(
            name = "roles_user", // Intermediate table
            joinColumns = @JoinColumn(name = "user_id"), // FK to users
            inverseJoinColumns = @JoinColumn(name = "role_id") // FK to roles
    )
    private List<Role> role = new LinkedList<>();

    //----------------------------------PATIENT------------------------------------
    @Column(name = "unique_identification", nullable = true, unique = true)
    private String uniqueIdentification;

    @Column(name = "insurance_paid_last_6_months", nullable = true)
    private Boolean insurancePaidLast6Months;

    @OneToMany(mappedBy = "patient")
    private Set<SickDay> sickDay = new HashSet<>();

    @OneToMany(mappedBy = "patient")
    private Set<PatientIllnessHistory> patientIllnessHistory = new HashSet<>();

    @OneToMany(mappedBy = "patient")
    private Set<DoctorAppointment> doctorAppointment = new HashSet<>();

    //--------------------------------DOCTOR------------------------------------
    @Column(name = "unique_id", nullable = true, unique = true)
    private String uniqueId;

    @Column(name = "is_personal_doctor", nullable = true)
    private Boolean isPersonalDoctor;

    @OneToMany(mappedBy = "doctor")
    private Set<SickDay> sickDays = new HashSet<>();

    @OneToMany(mappedBy = "doctor")
    private Set<DoctorAppointment> doctorAppointments = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_has_doctor_specialization",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "doctor_specialization_id")
    )
    private Set<DoctorSpecialization> specializations = new HashSet<>();
}
