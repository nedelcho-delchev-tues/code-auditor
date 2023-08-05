package com.code.auditor.exceptions;

import com.code.auditor.dtos.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<MessageResponse> handleInvalidEmailException(InvalidEmailException e) {
        MessageResponse errorResponse = new MessageResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<MessageResponse> handleInvalidEmailException(InvalidPasswordException e) {
        MessageResponse errorResponse = new MessageResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // TODO Add other exception handlers for registration
}