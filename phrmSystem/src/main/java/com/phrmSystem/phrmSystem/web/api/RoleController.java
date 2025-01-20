package com.phrmSystem.phrmSystem.controller;

import com.phrmSystem.phrmSystem.data.entity.Role;
import com.phrmSystem.phrmSystem.dto.RoleDTO;
import com.phrmSystem.phrmSystem.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing roles.
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Creates a new role.
     *
     * @param role the role entity to create.
     * @return the created role as a ResponseEntity.
     */
    @PostMapping
    public ResponseEntity<?> createRole(@Valid @RequestBody Role role) {
        try {
            Role createdRole = roleService.createRole(role);
            return ResponseEntity.status(201).body(createdRole);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Updates an existing role.
     *
     * @param id   the ID of the role to update.
     * @param role the updated role details.
     * @return the updated role as a ResponseEntity.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @Valid @RequestBody Role role) {
        try {
            Role updatedRole = roleService.updateRole(id, role);
            return ResponseEntity.ok(updatedRole);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Deletes a role by ID.
     *
     * @param id the ID of the role to delete.
     * @return a ResponseEntity indicating the status of the operation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Retrieves a role by ID.
     *
     * @param id the ID of the role to retrieve.
     * @return the role as a RoleDTO wrapped in a ResponseEntity.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable Long id) {
        try {
            Role role = roleService.getRoleById(id);
            RoleDTO roleDTO = new RoleDTO(role.getId(), role.getRoleName(), role.getDescription());
            return ResponseEntity.ok(roleDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Retrieves all roles.
     *
     * @return a list of RoleDTOs as a ResponseEntity.
     */
    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.getAllRoles()
                .stream()
                .map(role -> new RoleDTO(role.getId(), role.getRoleName(), role.getDescription()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }

    /**
     * Searches roles by a keyword in their name or description.
     *
     * @param keyword the keyword to search for.
     * @return a list of matching roles wrapped in a ResponseEntity.
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchRolesByKeyword(@RequestParam String keyword) {
        try {
            List<Role> roles = roleService.searchRolesByKeyword(keyword);
            return ResponseEntity.ok(roles);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Retrieves a role by its name.
     *
     * @param roleName the name of the role.
     * @return the role as an Optional wrapped in a ResponseEntity.
     */
    @GetMapping("/name/{roleName}")
    public ResponseEntity<?> getRoleByName(@PathVariable String roleName) {
        try {
            Optional<Role> role = roleService.getRoleByName(roleName);
            return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
