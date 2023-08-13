package com.code.auditor.controllers;

import com.code.auditor.configuration.JwtService;
import com.code.auditor.domain.User;
import com.code.auditor.dtos.MessageResponse;
import com.code.auditor.enums.Role;
import com.code.auditor.services.UserService;
import com.code.auditor.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class UserController {

    private final UserService adminService;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    public UserController(UserService adminService, AuthenticationService authenticationService, JwtService jwtService) {
        this.adminService = adminService;
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @GetMapping("user")
    public ResponseEntity<Object> getUserInfoFromJwt(){
        return ResponseEntity.ok(jwtService.getUserByRequest());
    }

    @GetMapping("admin/user-by-role/{role}")
    @PreAuthorize("hasRole('ADMIN') && hasAuthority('admin:read')")
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

    @PostMapping("admin/register-staff")
    @PreAuthorize("hasRole('ADMIN') && hasAuthority('admin:create')")
    public ResponseEntity<Object> registerStaff(@RequestBody User user) {
        return ResponseEntity.ok(authenticationService.register(user));
    }
}
