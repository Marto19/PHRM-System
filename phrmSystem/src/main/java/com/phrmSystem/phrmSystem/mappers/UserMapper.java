package com.phrmSystem.phrmSystem.mappers;

import com.phrmSystem.phrmSystem.data.entity.Role;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.dto.RoleDTO;
import com.phrmSystem.phrmSystem.dto.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().stream()
                        .map(role -> new RoleDTO(role.getId(), role.getRoleName(), role.getDescription()))
                        .collect(Collectors.toList())
        );
    }

    public static RoleDTO toRoleDTO(Role role) {
        return new RoleDTO(
                role.getId(),
                role.getRoleName(),
                role.getDescription()
        );
    }
}
//TODO: CONTINUE WITH THE SAME THING FOR THE OTHER ENTITIES
//TODO: FIND A WAY WHEN CREATING A USER IN KEYCLOAK, TO BE CREATED IN THE DATABASE TOO