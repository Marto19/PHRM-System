package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.Role;
import com.phrmSystem.phrmSystem.data.repo.RoleRepository;
import com.phrmSystem.phrmSystem.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private void validateRole(Role role){
        if (!StringUtils.hasText(role.getRoleName())) {
            throw new RuntimeException("Role name cannot be blank");
        } else if (roleRepository.findByRoleName(role.getRoleName()).isPresent()) {
            throw new RuntimeException("Role with that name already exists.");
        }
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRole(long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role with id " + id + "not found."));
    }

    @Override
    public Role createRole(Role role) {
        validateRole(role);
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Role updatedRole, long id) {

        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(("Role with id " + id + " not found")));

        validateRole(updatedRole);

        existingRole.setRoleName(updatedRole.getRoleName());
        existingRole.setDescription(updatedRole.getDescription());

        return roleRepository.save(existingRole);
    }

    @Override
    public void deleteRole(long id) {
        if(!roleRepository.existsById(id)){
            throw new RuntimeException("Role with id " + id + " not found, cannot delete.");
        }
        deleteRole(id);
    }
}
