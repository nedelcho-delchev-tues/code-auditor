package com.code.auditor.exceptions;

import com.code.auditor.dtos.MessageResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<MessageResponse> handleInvalidEmailException(InvalidEmailException e) {
        MessageResponse errorResponse = new MessageResponse(HttpStatus.BAD_REQUEST.value(), "Невалиден имейл");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<MessageResponse> handleInvalidEmailException(InvalidPasswordException e) {
        MessageResponse errorResponse = new MessageResponse(HttpStatus.BAD_REQUEST.value(), "Невалидна парола. Паролата трябва да е поне дълга поне 6 знака");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<MessageResponse> handleExpiredJwtException(ExpiredJwtException e) {
        MessageResponse errorResponse = new MessageResponse(HttpStatus.UNAUTHORIZED.value(), "Невалиден JWT токен");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(SubmissionSubmittedException.class)
    public ResponseEntity<MessageResponse> handleSubmissionSubmitted(SubmissionSubmittedException e) {
        MessageResponse errorResponse = new MessageResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

   @ExceptionHandler(NoSuchElementException.class)
   public ResponseEntity<MessageResponse> handleSubmissionSubmitted(NoSuchElementException e) {
       MessageResponse errorResponse = new MessageResponse(HttpStatus.NOT_FOUND.value(), "Не е намерен такъв елемент.");
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
   }


    // TODO Add other exception handlers for registration
}