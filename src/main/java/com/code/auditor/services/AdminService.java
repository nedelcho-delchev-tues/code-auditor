package com.code.auditor.services;

import com.code.auditor.domain.User;
import com.code.auditor.enums.Role;
import com.code.auditor.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId){
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new IllegalArgumentException("Потребител с това id не е намерен: " + userId);
        }
    }

    public List<User> getUserByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public void disableUser(Long userId){
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User fetchedUser = user.get();
            fetchedUser.setEnabled(false);
            userRepository.save(fetchedUser);
        } else {
            throw new IllegalArgumentException("Потребител с това id не е намерен: " + userId);
        }

    }
}
