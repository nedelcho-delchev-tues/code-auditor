package com.code.auditor.exceptions;

import com.code.auditor.dtos.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RegistrationExceptionHandler {

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<MessageResponse> handleInvalidEmailException(InvalidEmailException ex) {
        MessageResponse errorResponse = new MessageResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // TODO Add other exception handlers for registration
}