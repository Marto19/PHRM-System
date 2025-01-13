package com.phrmSystem.phrmSystem.data.entity;

import jakarta.persistence.*;
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
    private String illnessName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "patients_idpatient", nullable = false)
    private User patient;

    @OneToMany(mappedBy = "patientIllnessHistory")
    private Set<DoctorAppointment> doctorAppointment;
}
