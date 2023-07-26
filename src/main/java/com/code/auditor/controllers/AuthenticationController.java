package com.code.auditor.controllers;

import com.code.auditor.domain.Staff;
import com.code.auditor.dtos.AuthenticationResponse;
import com.code.auditor.dtos.AuthenticationRequest;
import com.code.auditor.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerStaff(@RequestBody Staff staff) {
        // maybe staff should be a DTO in the future
        return ResponseEntity.ok(authenticationService.registerStaff(staff));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticateStaff(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return ResponseEntity.ok("Logged out successfully");
        }
        return ResponseEntity.badRequest().body("Already logout or bad token");
    }
}
