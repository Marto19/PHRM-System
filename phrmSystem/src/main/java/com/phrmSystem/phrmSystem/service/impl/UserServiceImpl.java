package com.phrmSystem.phrmSystem.service.impl;

import com.phrmSystem.phrmSystem.data.entity.User;
import com.phrmSystem.phrmSystem.data.repo.UserRepository;
import com.phrmSystem.phrmSystem.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long userId, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setRole(updatedUser.getRole());
            return userRepository.save(user);
        }
        throw new RuntimeException("User not found with id: " + userId);
    }

    @Override
    public void deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("User not found with id: " + userId));
    }

    @Override
    public List<User> getUsersByRoleName(String roleName) {
        return userRepository.findUsersByRoleName(roleName);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
