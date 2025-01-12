package com.phrmSystem.phrmSystem.service;

import com.phrmSystem.phrmSystem.data.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    Role createRole(Role role);

    Role updateRole(Long roleId, Role updatedRole);

    void deleteRole(Long roleId);

    Role getRoleById(Long roleId);

    List<Role> getAllRoles();

    Optional<Role> getRoleByName(String roleName);

    List<Role> searchRolesByKeyword(String keyword);
}
