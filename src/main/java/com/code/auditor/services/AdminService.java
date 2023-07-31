package com.code.auditor.services;

import com.code.auditor.domain.User;
import com.code.auditor.enums.Role;
import com.code.auditor.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUserByRole(Role role) {
        return userRepository.findByRole(role);
    }

}
