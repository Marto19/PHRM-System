package com.phrmSystem.phrmSystem.service;

import com.phrmSystem.phrmSystem.data.entity.Role;
import com.phrmSystem.phrmSystem.data.repo.RoleRepository;
import com.phrmSystem.phrmSystem.service.impl.RoleServiceImpl;

import java.util.List;

public interface RoleService {

    List<Role> getRoles();

    Role getRole(long id);

    Role createRole(Role role);

    Role updateRole(Role role, long id);

    void deleteRole(long id);

}
