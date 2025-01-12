package com.phrmSystem.phrmSystem.service;

import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.dto.UserDTO;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User updateUser(Long userId, User updatedUser);

    void deleteUser(Long userId);

    UserDTO getUserById(Long userId);

    List<User> getUsersByRoleName(String roleName);

    List<UserDTO> getAllUsers();
}
