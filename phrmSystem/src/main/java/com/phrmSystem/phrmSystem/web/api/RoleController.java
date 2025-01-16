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

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
        return ResponseEntity.ok(roleService.createRole(role));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @Valid @RequestBody Role role) {
        return ResponseEntity.ok(roleService.updateRole(id, role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        RoleDTO roleDTO = new RoleDTO(role.getId(), role.getRoleName(), role.getDescription());
        return ResponseEntity.ok(roleDTO);
    }


    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.getAllRoles()
                .stream()
                .map(role -> new RoleDTO(role.getId(), role.getRoleName(), role.getDescription()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }


    @GetMapping("/search")
    public ResponseEntity<List<Role>> searchRolesByKeyword(@RequestParam String keyword) {
        return ResponseEntity.ok(roleService.searchRolesByKeyword(keyword));
    }

    @GetMapping("/name/{roleName}")
    public ResponseEntity<Optional<Role>> getRoleByName(@PathVariable String roleName) {
        return ResponseEntity.ok(roleService.getRoleByName(roleName));
    }
}
