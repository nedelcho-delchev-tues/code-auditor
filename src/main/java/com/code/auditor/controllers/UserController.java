package com.code.auditor.controllers;

import com.code.auditor.configuration.JwtService;
import com.code.auditor.domain.User;
import com.code.auditor.dtos.ChangePasswordDTO;
import com.code.auditor.dtos.MessageResponse;
import com.code.auditor.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final JwtService jwtService;
    private final UserService userService;

    public UserController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<Object> getUserInfoFromJwt(){
        return ResponseEntity.ok(jwtService.getUserByRequest());
    }

    @GetMapping("/count_submissions")
    public ResponseEntity<Object> countSubmissions(){
        User user = jwtService.getUserByRequest();
        return ResponseEntity.ok(userService.countSubmissions(user.getId()));
    }

    @PostMapping("/{id}/change_password")
    public ResponseEntity<Object> changePassword(@PathVariable Long id, @RequestBody ChangePasswordDTO changePasswordDTO){
        userService.changePassword(id, changePasswordDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new MessageResponse(HttpStatus.OK.value(), "Паролата беше обновенна успешно."));
    }
}
