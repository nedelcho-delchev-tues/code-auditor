package com.code.auditor.services;

import com.code.auditor.domain.User;
import com.code.auditor.enums.Role;
import com.code.auditor.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    //TODO Rename UserService and change controller privileges
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUserByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public static String buildNameAndTitle(String... strings) {
        StringBuilder result = new StringBuilder();
        boolean isFirstNonNull = true;

        for (String str : strings) {
            if (str != null) {
                if (!isFirstNonNull) {
                    result.append(" ");
                } else {
                    isFirstNonNull = false;
                }
                result.append(str);
            }
        }
        return result.toString();
    }

}
