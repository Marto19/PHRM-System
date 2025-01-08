package com.phrmSystem.phrmSystem.data.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "doctor_appointments_iddoctor_appointments", nullable = false)
    private DoctorAppointment doctorAppointment;
}
