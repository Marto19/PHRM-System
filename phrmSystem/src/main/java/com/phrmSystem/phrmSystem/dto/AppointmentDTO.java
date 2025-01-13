package com.phrmSystem.phrmSystem.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppointmentDTO {
    private Long id;
    private String patientName;
    private LocalDateTime appointmentDate;
}
