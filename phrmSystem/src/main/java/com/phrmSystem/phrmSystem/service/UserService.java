package com.phrmSystem.phrmSystem.service;

import com.phrmSystem.phrmSystem.data.entity.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User updateUser(Long userId, User updatedUser);

    void deleteUser(Long userId);

    User getUserById(Long userId);

    List<User> getUsersByRoleName(String roleName);

    List<User> getAllUsers();
}
