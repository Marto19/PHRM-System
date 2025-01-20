package com.phrmSystem.phrmSystem.data.repo;

import com.phrmSystem.phrmSystem.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing User entities.
 * Extends JpaRepository to provide CRUD operations and custom queries.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find all users by role
    @Query("SELECT u FROM User u JOIN u.role r WHERE r.roleName = :roleName")
    List<User> findUsersByRoleName(String roleName);

    // Find user by ID (generic)
    Optional<User> findById(Long id);

    /**
     * Checks if a User exists with the specified unique identification.
     *
     * @param uniqueIdentification the unique identification to search for.
     * @return true if a User with the given unique identification exists, false otherwise.
     */
    boolean existsByUniqueIdentification(String uniqueIdentification);

    Optional<User> findByUniqueIdentification(String uniqueIdentification);

}
