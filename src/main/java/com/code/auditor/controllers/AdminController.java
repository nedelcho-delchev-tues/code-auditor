package com.code.auditor.controllers;

import com.code.auditor.domain.User;
import com.code.auditor.dtos.MessageResponse;
import com.code.auditor.enums.Role;
import com.code.auditor.exceptions.InvalidEmailException;
import com.code.auditor.exceptions.InvalidPasswordException;
import com.code.auditor.services.AdminService;
import com.code.auditor.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN') && hasRole('PROFESSOR')")
public class AdminController {

    private final AdminService adminService;
    private final AuthenticationService authenticationService;

    public AdminController(AdminService adminService, AuthenticationService authenticationService) {
        this.adminService = adminService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/user-by-role/{role}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<Object> getAllUserByRole(@PathVariable String role) {
        try {
            Role userRole = Role.valueOf(role.toUpperCase());
            List<User> users = adminService.getUserByRole(userRole);
            if (users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new MessageResponse(HttpStatus.BAD_REQUEST.value(), "Грешка с подадената роля"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new MessageResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Неочаквана грешка:" + e.getMessage()));
        }
    }

    @PostMapping("/register-staff")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<Object> registerStaff(@RequestBody User user) {
        try {
            return ResponseEntity.ok(authenticationService.register(user));
        } catch (InvalidEmailException | InvalidPasswordException e) {
            MessageResponse errorResponse = new MessageResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
