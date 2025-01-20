package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.controller.RoleController;
import com.phrmSystem.phrmSystem.data.entity.Role;
import com.phrmSystem.phrmSystem.dto.RoleDTO;
import com.phrmSystem.phrmSystem.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class RoleControllerTest {

    @Mock
    private RoleService roleService;

    private com.phrmSystem.phrmSystem.controller.RoleController roleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roleController = new RoleController(roleService);
    }

    @Test
    void createRole_Success() {
        Role role = new Role();
        role.setRoleName("ADMIN");

        when(roleService.createRole(role)).thenReturn(role);

        ResponseEntity<?> response = roleController.createRole(role);

        assertEquals(CREATED, response.getStatusCode());
        assertEquals(role, response.getBody());
        verify(roleService, times(1)).createRole(role);
    }

    @Test
    void createRole_Failure() {
        Role role = new Role();

        when(roleService.createRole(role)).thenThrow(new RuntimeException("Role creation failed."));

        ResponseEntity<?> response = roleController.createRole(role);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Role creation failed.", response.getBody());
        verify(roleService, times(1)).createRole(role);
    }

    @Test
    void updateRole_Success() {
        Role role = new Role();
        role.setRoleName("ADMIN");

        when(roleService.updateRole(1L, role)).thenReturn(role);

        ResponseEntity<?> response = roleController.updateRole(1L, role);

        assertEquals(OK, response.getStatusCode());
        assertEquals(role, response.getBody());
        verify(roleService, times(1)).updateRole(1L, role);
    }

    @Test
    void updateRole_Failure() {
        Role role = new Role();

        when(roleService.updateRole(1L, role)).thenThrow(new RuntimeException("Role update failed."));

        ResponseEntity<?> response = roleController.updateRole(1L, role);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Role update failed.", response.getBody());
        verify(roleService, times(1)).updateRole(1L, role);
    }

    @Test
    void deleteRole_Success() {
        doNothing().when(roleService).deleteRole(1L);

        ResponseEntity<?> response = roleController.deleteRole(1L);

        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(roleService, times(1)).deleteRole(1L);
    }

    @Test
    void deleteRole_Failure() {
        doThrow(new RuntimeException("Role deletion failed.")).when(roleService).deleteRole(1L);

        ResponseEntity<?> response = roleController.deleteRole(1L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Role deletion failed.", response.getBody());
        verify(roleService, times(1)).deleteRole(1L);
    }

    @Test
    void getRoleById_Success() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName("ADMIN");

        when(roleService.getRoleById(1L)).thenReturn(role);

        ResponseEntity<?> response = roleController.getRoleById(1L);

        assertEquals(OK, response.getStatusCode());
        RoleDTO roleDTO = (RoleDTO) response.getBody();
        assertEquals("ADMIN", roleDTO.getRoleName());
        verify(roleService, times(1)).getRoleById(1L);
    }

    @Test
    void getRoleById_Failure() {
        when(roleService.getRoleById(1L)).thenThrow(new RuntimeException("Role not found."));

        ResponseEntity<?> response = roleController.getRoleById(1L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Role not found.", response.getBody());
        verify(roleService, times(1)).getRoleById(1L);
    }

    @Test
    void getAllRoles_Success() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName("ADMIN");

        when(roleService.getAllRoles()).thenReturn(List.of(role));

        ResponseEntity<List<RoleDTO>> response = roleController.getAllRoles();

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("ADMIN", response.getBody().get(0).getRoleName());
        verify(roleService, times(1)).getAllRoles();
    }

    @Test
    void searchRolesByKeyword_Success() {
        Role role = new Role();
        role.setRoleName("ADMIN");

        when(roleService.searchRolesByKeyword("ADMIN")).thenReturn(List.of(role));

        ResponseEntity<?> response = roleController.searchRolesByKeyword("ADMIN");

        assertEquals(OK, response.getStatusCode());
        List<Role> roles = (List<Role>) response.getBody();
        assertEquals(1, roles.size());
        assertEquals("ADMIN", roles.get(0).getRoleName());
        verify(roleService, times(1)).searchRolesByKeyword("ADMIN");
    }

    @Test
    void getRoleByName_Success() {
        Role role = new Role();
        role.setRoleName("ADMIN");

        when(roleService.getRoleByName("ADMIN")).thenReturn(Optional.of(role));

        ResponseEntity<?> response = roleController.getRoleByName("ADMIN");

        assertEquals(OK, response.getStatusCode());
        Role returnedRole = (Role) response.getBody();
        assertEquals("ADMIN", returnedRole.getRoleName());
        verify(roleService, times(1)).getRoleByName("ADMIN");
    }

    @Test
    void getRoleByName_Failure_NotFound() {
        when(roleService.getRoleByName("ADMIN")).thenReturn(Optional.empty());

        ResponseEntity<?> response = roleController.getRoleByName("ADMIN");

        assertEquals(NOT_FOUND, response.getStatusCode());
        verify(roleService, times(1)).getRoleByName("ADMIN");
    }
}
