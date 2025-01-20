package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.repo.UserRepository;
import com.phrmSystem.phrmSystem.dto.UserDTO;
import com.phrmSystem.phrmSystem.mappers.UserMapper;
import com.phrmSystem.phrmSystem.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the UserService interface for managing User-related operations.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new User after validating inputs and checking for duplicates.
     *
     * @param user the User to be created.
     * @return the created User entity.
     */
    @Override
    @Transactional
    public User createUser(User user) {
        validateUser(user);
        checkForDuplicateUniqueFields(user);
        return userRepository.save(user);
    }

    /**
     * Updates an existing User with new details.
     *
     * @param userId       the ID of the User to update.
     * @param updatedUser  the new User details.
     * @return the updated User entity.
     */
    @Override
    @Transactional
    public User updateUser(Long userId, User updatedUser) {
        validateUser(updatedUser);

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (updatedUser.getUniqueIdentification() != null
                && !updatedUser.getUniqueIdentification().equals(existingUser.getUniqueIdentification())) {
            checkForDuplicateUniqueIdentification(updatedUser.getUniqueIdentification());
        }

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setRole(updatedUser.getRole());
        existingUser.setUniqueIdentification(updatedUser.getUniqueIdentification());
        existingUser.setInsurancePaidLast6Months(updatedUser.getInsurancePaidLast6Months());
        return userRepository.save(existingUser);
    }

    /**
     * Deletes a User by their ID.
     *
     * @param userId the ID of the User to delete.
     */
    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Check for dependencies
        if (!user.getDoctorAppointments().isEmpty()) {
            throw new RuntimeException("Cannot delete user with active appointments. Please reassign or delete appointments first.");
        }

        try {
            userRepository.deleteById(userId);
        } catch (RuntimeException ex) {
            throw new RuntimeException("Cannot delete user with active dependencies");
        }
    }


    /**
     * Retrieves a User by their ID and maps them to a DTO.
     *
     * @param id the ID of the User.
     * @return the UserDTO representation of the User.
     */
    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        return UserMapper.toDTO(user);
    }

    /**
     * Retrieves a list of Users by their role name.
     *
     * @param roleName the role name to filter by.
     * @return a list of Users with the specified role name.
     */
    @Override
    public List<User> getUsersByRoleName(String roleName) {
        if (roleName == null || roleName.isBlank()) {
            throw new IllegalArgumentException("Role name cannot be null or empty.");
        }
        return userRepository.findUsersByRoleName(roleName);
    }

    /**
     * Retrieves all Users and maps them to a list of DTOs.
     *
     * @return a list of UserDTOs.
     */
    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void validateUser(User user) {
        if (user.getUniqueIdentification() != null && userRepository.existsByUniqueIdentification(user.getUniqueIdentification())) {
            throw new RuntimeException("User with unique identification " + user.getUniqueIdentification() + " already exists.");
        }
        if (user.getFirstName() == null || user.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or empty.");
        }
        if (user.getLastName() == null || user.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or empty.");
        }
        if (user.getRole() == null || user.getRole().isEmpty()) {
            throw new IllegalArgumentException("User role must be provided.");
        }
    }


    private void checkForDuplicateUniqueFields(User user) {
        if (user.getUniqueIdentification() != null) {
            checkForDuplicateUniqueIdentification(user.getUniqueIdentification());
        }
    }

    private void checkForDuplicateUniqueIdentification(String uniqueIdentification) {
        if (userRepository.existsByUniqueIdentification(uniqueIdentification)) {
            throw new RuntimeException("User with unique identification " + uniqueIdentification + " already exists.");
        }
    }
}
