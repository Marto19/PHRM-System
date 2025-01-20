package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.Role;
import com.phrmSystem.phrmSystem.data.repo.RoleRepository;
import com.phrmSystem.phrmSystem.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the RoleService interface, responsible for managing Role-related operations.
 */
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Creates a new Role after validating the input.
     *
     * @param role the Role entity to create.
     * @return the created Role entity.
     * @throws IllegalArgumentException if validation fails.
     */
    @Override
    @Transactional
    public Role createRole(Role role) {
        validateRole(role);
        checkForDuplicateRoleName(role.getRoleName());
        try {
            return roleRepository.save(role);
        } catch (Exception ex) {
            throw new RuntimeException("A role with the name '" + role.getRoleName() + "' already exists. Please use a different name.");
        }
    }


    /**
     * Updates an existing Role by ID.
     *
     * @param roleId      the ID of the Role to update.
     * @param updatedRole the updated Role entity.
     * @return the updated Role entity.
     * @throws RuntimeException if the Role with the given ID is not found.
     * @throws IllegalArgumentException if validation fails.
     */
    @Override
    @Transactional
    public Role updateRole(Long roleId, Role updatedRole) {
        validateRole(updatedRole);

        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        if (!updatedRole.getRoleName().equals(existingRole.getRoleName())) {
            checkForDuplicateRoleName(updatedRole.getRoleName());
        }

        existingRole.setRoleName(updatedRole.getRoleName());
        existingRole.setDescription(updatedRole.getDescription());
        return roleRepository.save(existingRole);
    }

    /**
     * Deletes a Role by ID if it exists.
     *
     * @param roleId the ID of the Role to delete.
     * @throws RuntimeException if the Role with the given ID is not found.
     * @throws RuntimeException if the Role is assigned to users.
     */
    @Override
    @Transactional
    public void deleteRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        if (!role.getUser().isEmpty()) {
            throw new RuntimeException("Cannot delete role as it is assigned to users. Please unassign the role from all users before deleting.");
        }

        try {
            roleRepository.deleteById(roleId);
        } catch (Exception ex) {
            throw new RuntimeException("Cannot delete role due to active dependencies. Ensure no users or other records reference this role.");
        }
    }


    /**
     * Retrieves a Role by ID.
     *
     * @param roleId the ID of the Role to retrieve.
     * @return the Role entity.
     * @throws RuntimeException if the Role with the given ID is not found.
     */
    @Override
    public Role getRoleById(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
    }

    /**
     * Retrieves all Roles.
     *
     * @return a list of Role entities.
     */
    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Retrieves a Role by its name.
     *
     * @param roleName the name of the Role to retrieve.
     * @return an Optional containing the Role entity if found, or empty if not found.
     */
    @Override
    public Optional<Role> getRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    /**
     * Searches for Roles by a keyword in their name.
     *
     * @param keyword the keyword to search for.
     * @return a list of Role entities that match the keyword.
     */
    @Override
    public List<Role> searchRolesByKeyword(String keyword) {
        return roleRepository.findByRoleNameContaining(keyword);
    }

    /**
     * Validates a Role entity for required fields and constraints.
     *
     * @param role the Role entity to validate.
     * @throws IllegalArgumentException if validation fails.
     */
    private void validateRole(Role role) {
        if (role.getRoleName() == null || role.getRoleName().isBlank()) {
            throw new IllegalArgumentException("Role name cannot be null or empty.");
        }
        if (role.getRoleName().length() < 3 || role.getRoleName().length() > 20) {
            throw new IllegalArgumentException("Role name must be between 3 and 20 characters.");
        }
        if (role.getDescription() != null && role.getDescription().length() > 100) {
            throw new IllegalArgumentException("Description must not exceed 100 characters.");
        }
    }

    /**
     * Checks if a Role name already exists.
     *
     * @param roleName the name of the Role to check.
     * @throws RuntimeException if a Role with the given name already exists.
     */
    private void checkForDuplicateRoleName(String roleName) {
        if (roleRepository.findByRoleName(roleName).isPresent()) {
            throw new RuntimeException("Role with name '" + roleName + "' already exists.");
        }
    }
}
