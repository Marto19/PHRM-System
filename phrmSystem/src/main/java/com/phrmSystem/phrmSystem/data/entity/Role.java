package com.phrmSystem.phrmSystem.data.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Table(name = "roles", uniqueConstraints = {
        @UniqueConstraint(columnNames = "role_name", name = "unique_role_name")
})
public class Role extends BaseEntity {

    @Column(name = "role_name", nullable = false, unique = true)
    @NotBlank(message = "The role name cannot be blank.")
    @Size(min = 3, max = 20, message = "The role name must be between 3 and 20 characters.")
    private String roleName;

    @Column(name = "description")
    @Size(max = 100, message = "The description must not exceed 100 characters.")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Role(String roleName) {
        this.roleName = roleName;
    }
}

