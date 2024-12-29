package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.data.entity.Role;
import com.phrmSystem.phrmSystem.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasRole('client_admin')")
    public List<Role> getRoles() {
        return roleService.getRoles();
    }

    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable long id) {
        return roleService.getRole(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Sets HTTP status to 201
    public Role createRole(@Valid @RequestBody Role role) {
        return roleService.createRole(role);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Sets HTTP status to 204
    public Role updateRole(@Valid @RequestBody Role role, @PathVariable long id) {
        return roleService.updateRole(role, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Sets HTTP status to 204
    public void deleteRole(@PathVariable long id) {
        roleService.deleteRole(id);
    }
}

