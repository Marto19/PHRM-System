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
@Table(name = "patient_illness_history")
public class PatientIllnessHistory extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "patients_idpatient", nullable = false)
    private Patient patient;

    @OneToMany(mappedBy = "patientIllnessHistory")
    private Set<DoctorAppointment> doctorAppointment;
}
