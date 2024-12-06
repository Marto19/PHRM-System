package com.phrmSystem.phrmSystem.data.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Table(name = "roles")
public class Role extends BaseEntity {

    @Column(name = "role_name")
    @NotBlank(message = "The role name cannot be blank.")
    @Size(min = 3, max = 20, message = "The role name cannot be below 3 and exceed 20 characters.")
    private String role_name;

    @Column(name = "description")
    @Size(max = 100, message = "The description must not exceed 100 characters.")
    private String description;

    public Role(String role_name) {
        this.role_name = role_name;
    }
}
