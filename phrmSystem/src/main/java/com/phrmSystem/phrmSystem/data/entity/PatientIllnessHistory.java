package com.phrmSystem.phrmSystem.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "patient_illness_history")
public class PatientIllnessHistory extends BaseEntity {

    @Column(name = "illness_name", nullable = false)
    @NotBlank(message = "Illness name is required")
    private String illnessName; //TODO:remove this

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patients_idpatient", nullable = false)
    private User patient;

    @OneToMany(mappedBy = "patientIllnessHistory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<DoctorAppointment> doctorAppointment;

}
