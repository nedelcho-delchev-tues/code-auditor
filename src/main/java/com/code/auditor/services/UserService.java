package com.code.auditor.services;

import com.code.auditor.domain.User;
import com.code.auditor.dtos.ChangePasswordDTO;
import com.code.auditor.exceptions.InvalidPasswordException;
import com.code.auditor.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.code.auditor.services.AuthenticationService.MIN_CHAR_FOR_PASSWORD;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void changePassword(Long id, ChangePasswordDTO changePasswordDTO){
        String newEncodedPassword = passwordEncoder.encode(changePasswordDTO.getNewPassword());

        if (changePasswordDTO.getNewPassword().length() < MIN_CHAR_FOR_PASSWORD){
            throw new InvalidPasswordException("Невалидна парола. Паролата трябва да е минимум 6 символа");
        }

        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getNewPasswordRepeat())){
            throw new InvalidPasswordException("Паролите трябва да съвпадат.");
        }

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User presentUser = user.get();

            if (!passwordEncoder.matches(changePasswordDTO.getPassword(), presentUser.getPassword())){
                throw new InvalidPasswordException("Сегашна парола не съвпада с вече съществуващата.");
            }

            presentUser.setPassword(newEncodedPassword);
            userRepository.save(presentUser);
        } else {
            throw new IllegalArgumentException("Потребител с това id не е намерен: " + id);
        }
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
