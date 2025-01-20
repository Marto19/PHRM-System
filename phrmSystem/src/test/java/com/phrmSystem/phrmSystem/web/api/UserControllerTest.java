package com.phrmSystem.phrmSystem.web.api;

import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.dto.UserDTO;
import com.phrmSystem.phrmSystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);
    }

    @Test
    void createUser_Success() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userService.createUser(user)).thenReturn(user);

        ResponseEntity<?> response = userController.createUser(user);

        assertEquals(CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).createUser(user);
    }

    @Test
    void createUser_Failure() {
        User user = new User();
        user.setFirstName("John");

        when(userService.createUser(user)).thenThrow(new IllegalArgumentException("Last name cannot be null or empty."));

        ResponseEntity<?> response = userController.createUser(user);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Last name cannot be null or empty.", response.getBody());
        verify(userService, times(1)).createUser(user);
    }

    @Test
    void updateUser_Success() {
        User user = new User();
        user.setFirstName("Jane");

        when(userService.updateUser(1L, user)).thenReturn(user);

        ResponseEntity<?> response = userController.updateUser(1L, user);

        assertEquals(OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).updateUser(1L, user);
    }

    @Test
    void updateUser_Failure_NotFound() {
        User user = new User();
        user.setFirstName("Jane");

        when(userService.updateUser(1L, user)).thenThrow(new RuntimeException("User not found with id: 1"));

        ResponseEntity<?> response = userController.updateUser(1L, user);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("User not found with id: 1", response.getBody());
        verify(userService, times(1)).updateUser(1L, user);
    }

    @Test
    void deleteUser_Success() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<?> response = userController.deleteUser(1L);

        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void deleteUser_Failure() {
        doThrow(new RuntimeException("Cannot delete user with active dependencies"))
                .when(userService).deleteUser(1L);

        ResponseEntity<?> response = userController.deleteUser(1L);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Cannot delete user with active dependencies", response.getBody());
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void getUserById_Success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("John");

        when(userService.getUserById(1L)).thenReturn(userDTO);

        ResponseEntity<?> response = userController.getUserById(1L);

        assertEquals(OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void getUserById_Failure() {
        when(userService.getUserById(1L)).thenThrow(new RuntimeException("User not found with ID: 1"));

        ResponseEntity<?> response = userController.getUserById(1L);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("User not found with ID: 1", response.getBody());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void getAllUsers_Success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("John");

        when(userService.getAllUsers()).thenReturn(List.of(userDTO));

        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(userDTO, response.getBody().get(0));
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUsersByRoleName_Success() {
        User user = new User();
        user.setFirstName("John");

        when(userService.getUsersByRoleName("ADMIN")).thenReturn(List.of(user));

        ResponseEntity<?> response = userController.getUsersByRoleName("ADMIN");

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
        verify(userService, times(1)).getUsersByRoleName("ADMIN");
    }

    @Test
    void getUsersByRoleName_Failure() {
        when(userService.getUsersByRoleName(null)).thenThrow(new IllegalArgumentException("Role name cannot be null or empty."));

        ResponseEntity<?> response = userController.getUsersByRoleName(null);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Role name cannot be null or empty.", response.getBody());
        verify(userService, times(1)).getUsersByRoleName(null);
    }
}
