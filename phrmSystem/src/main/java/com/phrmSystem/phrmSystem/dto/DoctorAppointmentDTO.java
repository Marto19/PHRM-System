package com.phrmSystem.phrmSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAppointmentDTO {
    private Long id;
    private LocalDateTime date;
    private Long doctorId;
    private Long patientId;
}
