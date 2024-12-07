package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.data.entity.Role;
import com.phrmSystem.phrmSystem.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/roles")
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public List<Role> getRoless(){
        return roleService.getRoles();
    }

    @GetMapping("/{id}")
    public Role getRoleById(long id){
        return roleService.getRole(id);
    }

    @PostMapping
    public Role createRole(@RequestBody Role role){
        return roleService.createRole(role);
    }

    @PutMapping("/{id}")
    public Role updateRole(@RequestBody Role role, long id){
        return roleService.updateRole(role, id);
    }

    @DeleteMapping("/{id}")
    public void deleteRole(long id){
        roleService.deleteRole(id);
    }
}
