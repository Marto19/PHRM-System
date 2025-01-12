package com.phrmSystem.phrmSystem.data.repo;

import com.phrmSystem.phrmSystem.data.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // The following CRUD methods are already provided by JpaRepository:
    // - Role save(Role entity)
    // - Optional<Role> findById(Long id)
    // - List<Role> findAll()
    // - void deleteById(Long id)
    // - long count()
    // - boolean existsById(Long id)

    // You can also define custom query methods based on entity fields:
    @Query("SELECT r FROM Role r WHERE r.roleName = :roleName")
    Optional<Role> findByRoleName(@Param("roleName") String roleName);


    // Example of another custom query method:
    List<Role> findByRoleNameContaining(String keyword);

    boolean existsByRoleName(String doctor);

}
