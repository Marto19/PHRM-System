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
@Table(name = "sick_days")
public class SickDay extends BaseEntity {

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "number_of_days", nullable = false)
    private int numberOfDays;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Diagnosis> diagnosis;

    @ManyToOne(fetch = FetchType.EAGER)
    private User doctor;
}
