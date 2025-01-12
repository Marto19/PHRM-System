package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.Role;
import com.phrmSystem.phrmSystem.data.repo.RoleRepository;
import com.phrmSystem.phrmSystem.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Long roleId, Role updatedRole) {
        Optional<Role> existingRole = roleRepository.findById(roleId);
        if (existingRole.isPresent()) {
            Role role = existingRole.get();
            role.setRoleName(updatedRole.getRoleName());
            role.setDescription(updatedRole.getDescription());
            return roleRepository.save(role);
        }
        throw new RuntimeException("Role not found with id: " + roleId);
    }

    @Override
    public void deleteRole(Long roleId) {
        if (roleRepository.existsById(roleId)) {
            roleRepository.deleteById(roleId);
        } else {
            throw new RuntimeException("Role not found with id: " + roleId);
        }
    }

    @Override
    public Role getRoleById(Long roleId) {
        return roleRepository.findById(roleId).orElseThrow(() ->
                new RuntimeException("Role not found with id: " + roleId));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> getRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public List<Role> searchRolesByKeyword(String keyword) {
        return roleRepository.findByRoleNameContaining(keyword);
    }
}
