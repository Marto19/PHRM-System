package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.Role;
import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.repo.UserRepository;
import com.phrmSystem.phrmSystem.dto.UserDTO;
import com.phrmSystem.phrmSystem.mappers.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    private UserRepository userRepository;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void createUser_Success() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(List.of(new Role("USER")));
        user.setUniqueIdentification("12345");

        when(userRepository.existsByUniqueIdentification("12345")).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertEquals("John", createdUser.getFirstName());
        assertEquals("Doe", createdUser.getLastName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser_Failure_DuplicateUniqueIdentification() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(List.of(new Role("USER")));
        user.setUniqueIdentification("12345");

        when(userRepository.existsByUniqueIdentification("12345")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.createUser(user));
        assertEquals("User with unique identification 12345 already exists.", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_Success() {
        // Create a mock user for deletion
        User user = new User();
        user.setId(1L);

        // Mock repository behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Test the deletion
        assertDoesNotThrow(() -> userService.deleteUser(1L));

        // Verify interactions
        verify(userRepository, times(1)).deleteById(1L);
    }


    @Test
    void deleteUser_Failure_UserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
        assertEquals("User not found with id: 1", exception.getMessage());

        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteUser_Failure_DependentRecords() {
        // Create a mock user
        User user = new User();
        user.setId(1L);

        // Mock repository behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doThrow(new RuntimeException("Cannot delete user with active dependencies"))
                .when(userRepository).deleteById(1L);

        // Test the deletion and assert exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
        assertEquals("Cannot delete user with active dependencies", exception.getMessage());

        // Verify interactions
        verify(userRepository, times(1)).deleteById(1L);
    }


    @Test
    void getUserById_Success() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.getUserById(1L);

        assertEquals("John", userDTO.getFirstName());
        assertEquals("Doe", userDTO.getLastName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_Failure_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
        assertEquals("User not found with ID: 1", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUsersByRoleName_Success() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(List.of(new Role("ADMIN")));

        when(userRepository.findUsersByRoleName("ADMIN")).thenReturn(List.of(user));

        List<User> users = userService.getUsersByRoleName("ADMIN");

        assertEquals(1, users.size());
        assertEquals("John", users.get(0).getFirstName());
        assertEquals("Doe", users.get(0).getLastName());

        verify(userRepository, times(1)).findUsersByRoleName("ADMIN");
    }

    @Test
    void getUsersByRoleName_Failure_InvalidRoleName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUsersByRoleName(""));
        assertEquals("Role name cannot be null or empty.", exception.getMessage());

        verify(userRepository, never()).findUsersByRoleName(anyString());
    }

    @Test
    void getAllUsers_Success() {
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");

        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Smith");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserDTO> userDTOs = userService.getAllUsers();

        assertEquals(2, userDTOs.size());
        assertEquals("John", userDTOs.get(0).getFirstName());
        assertEquals("Jane", userDTOs.get(1).getFirstName());

        verify(userRepository, times(1)).findAll();
    }
}
