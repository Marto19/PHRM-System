package com.phrmSystem.phrmSystem.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "patients")
public class Patient extends BaseEntity {

    @Column(name = "unique_identification", nullable = false, unique = true)
    private String uniqueIdentification;

    @Column(name = "insurance_paid_last_6_months", nullable = false)
    private boolean insurancePaidLast6Months;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "personal_doctor_id", nullable = false)
    private Doctor personalDoctor;
}
