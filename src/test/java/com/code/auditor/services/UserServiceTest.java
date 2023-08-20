package com.code.auditor.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.code.auditor.domain.User;
import com.code.auditor.dtos.ChangePasswordDTO;
import com.code.auditor.exceptions.InvalidPasswordException;
import com.code.auditor.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    private final Long userId = 1L;
    private final User user = new User();
    private final String oldPassword = "oldPassword";
    private final String newPassword = "newPassword";

    @BeforeEach
    public void setup() {
        user.setId(userId);
        user.setPassword("encodedOldPassword");
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(oldPassword, newPassword, newPassword);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(oldPassword)).thenReturn("encodedOldPassword");
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        userService.changePassword(userId, changePasswordDTO);

        assertEquals("encodedNewPassword", user.getPassword());
    }

    @Test
    void shouldThrowInvalidPasswordExceptionWhenPasswordIsShort() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(oldPassword, "short", "short");

        assertThrows(InvalidPasswordException.class, () -> {
            userService.changePassword(userId, changePasswordDTO);
        });
    }

    @Test
    void shouldThrowInvalidPasswordExceptionWhenPasswordsDoNotMatch() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(oldPassword, newPassword, "differentPassword");

        assertThrows(InvalidPasswordException.class, () -> {
            userService.changePassword(userId, changePasswordDTO);
        });
    }

    @Test
    void shouldThrowInvalidPasswordExceptionWhenOldPasswordIsIncorrect() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("incorrectOldPassword", newPassword, newPassword);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("incorrectOldPassword")).thenReturn("incorrectEncodedOldPassword");

        assertThrows(InvalidPasswordException.class, () -> {
            userService.changePassword(userId, changePasswordDTO);
        });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUserNotFound() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(oldPassword, newPassword, newPassword);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            userService.changePassword(userId, changePasswordDTO);
        });
    }
}
