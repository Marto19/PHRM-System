package com.phrmSystem.phrmSystem.data.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "doctor_appointments")
public class DoctorAppointment extends BaseEntity {

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne
    @JoinColumn(name = "patient_ill_his_id")
    private PatientIllnessHistory patientIllnessHistory;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @OneToMany(mappedBy = "doctorAppointment")
    private Set<Diagnosis> diagnosis;

}
