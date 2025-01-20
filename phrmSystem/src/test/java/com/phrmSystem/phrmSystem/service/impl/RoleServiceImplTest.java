package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.Role;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.repo.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {
    private RoleRepository roleRepository;
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        roleRepository = mock(RoleRepository.class);
        roleService = new RoleServiceImpl(roleRepository);
    }

    @Test
    void createRole_Success() {
        Role role = new Role();
        role.setRoleName("ADMIN");
        role.setDescription("Administrator role");

        when(roleRepository.findByRoleName("ADMIN")).thenReturn(Optional.empty());
        when(roleRepository.save(role)).thenReturn(role);

        Role createdRole = roleService.createRole(role);

        assertEquals("ADMIN", createdRole.getRoleName());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void createRole_Failure_DuplicateName() {
        Role role = new Role();
        role.setRoleName("ADMIN");

        when(roleRepository.findByRoleName("ADMIN")).thenReturn(Optional.of(role));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> roleService.createRole(role));
        assertEquals("Role with name 'ADMIN' already exists.", exception.getMessage());
    }

    @Test
    void updateRole_Success() {
        Role existingRole = new Role();
        existingRole.setId(1L);
        existingRole.setRoleName("USER");

        Role updatedRole = new Role();
        updatedRole.setRoleName("ADMIN");
        updatedRole.setDescription("Updated description");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(existingRole));
        when(roleRepository.findByRoleName("ADMIN")).thenReturn(Optional.empty());
        when(roleRepository.save(existingRole)).thenReturn(existingRole);

        Role result = roleService.updateRole(1L, updatedRole);

        assertEquals("ADMIN", result.getRoleName());
        verify(roleRepository, times(1)).save(existingRole);
    }

    @Test
    void updateRole_Failure_NotFound() {
        Role updatedRole = new Role();
        updatedRole.setRoleName("ADMIN");

        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> roleService.updateRole(1L, updatedRole));
        assertEquals("Role not found with id: 1", exception.getMessage());
    }

    @Test
    void deleteRole_Success() {
        Role role = new Role();
        role.setId(1L);
        role.setUser(List.of());

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        assertDoesNotThrow(() -> roleService.deleteRole(1L));
        verify(roleRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteRole_Failure_AssignedToUsers() {
        Role role = new Role();
        role.setId(1L);
        role.setUser(List.of(new User()));

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> roleService.deleteRole(1L));
        assertEquals("Cannot delete role as it is assigned to users. Please unassign the role from all users before deleting.", exception.getMessage());
    }

    @Test
    void getRoleById_Success() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName("USER");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        Role result = roleService.getRoleById(1L);

        assertEquals("USER", result.getRoleName());
    }

    @Test
    void getRoleById_Failure_NotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> roleService.getRoleById(1L));
        assertEquals("Role not found with id: 1", exception.getMessage());
    }
}
