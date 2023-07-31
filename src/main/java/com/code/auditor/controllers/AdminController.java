package com.code.auditor.controllers;

import com.code.auditor.domain.User;
import com.code.auditor.dtos.AuthenticationResponse;
import com.code.auditor.enums.Role;
import com.code.auditor.services.AdminService;
import com.code.auditor.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<User>> getAllUserByRole(@PathVariable String role) {
        List<User> users = adminService.getUserByRole(Role.valueOf(role));
        return ResponseEntity.ok(users);
    }

    @PostMapping("/register-staff")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<AuthenticationResponse> registerStaff(@RequestBody User user) {
        return ResponseEntity.ok(authenticationService.register(user));
    }
}
