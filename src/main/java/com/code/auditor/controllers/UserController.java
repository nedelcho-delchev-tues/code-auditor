package com.code.auditor.controllers;

import com.code.auditor.configuration.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final JwtService jwtService;

    public UserController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping()
    public ResponseEntity<Object> getUserInfoFromJwt(){
        return ResponseEntity.ok(jwtService.getUserByRequest());
    }
}
