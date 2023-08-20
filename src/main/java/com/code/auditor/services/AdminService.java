package com.code.auditor.services;

import com.code.auditor.domain.User;
import com.code.auditor.dtos.AdminUserUpdateDTO;
import com.code.auditor.enums.Role;
import com.code.auditor.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

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

    public void updateUserById(Long userId, AdminUserUpdateDTO userUpdateDTO){
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User fetchedUser = user.get();

            assigneUserFromDTO(fetchedUser, userUpdateDTO);

            userRepository.save(fetchedUser);
        } else {
            throw new IllegalArgumentException("Потребител с това id не е намерен: " + userId);
        }
    }

    public void enableUser(Long userId){
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User fetchedUser = user.get();
            fetchedUser.setEnabled(true);
            userRepository.save(fetchedUser);
        } else {
            throw new IllegalArgumentException("Потребител с това id не е намерен: " + userId);
        }
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

    private void assigneUserFromDTO(User fetchedUser, AdminUserUpdateDTO userUpdateDTO){
        if (!userUpdateDTO.getFirstName().isEmpty()) {
            fetchedUser.setFirstName(userUpdateDTO.getFirstName());
        }

        if (!userUpdateDTO.getLastName().isEmpty()) {
            fetchedUser.setLastName(userUpdateDTO.getLastName());
        }

        if (userUpdateDTO.getRole() != null) {
            fetchedUser.setRole(userUpdateDTO.getRole());
        }

        if (userUpdateDTO.getTitle() != null && !userUpdateDTO.getTitle().isEmpty()) {
            fetchedUser.setTitle(userUpdateDTO.getTitle());
        }

        if (!userUpdateDTO.getFaculty().isEmpty()) {
            fetchedUser.setFaculty(userUpdateDTO.getFaculty());
        }

        if (!userUpdateDTO.getFacultyNumber().isEmpty()) {
            fetchedUser.setFacultyNumber(userUpdateDTO.getFacultyNumber());
        }

        if (!userUpdateDTO.getSpecialization().isEmpty()) {
            fetchedUser.setSpecialization(userUpdateDTO.getSpecialization());
        }

        if (!userUpdateDTO.getGroup().isEmpty()) {
            fetchedUser.setGroup(userUpdateDTO.getGroup());
        }

        if (!userUpdateDTO.getStream().isEmpty()) {
            fetchedUser.setStream(userUpdateDTO.getStream());
        }
    }
}
