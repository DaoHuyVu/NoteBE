package com.example.todoapp.exceptionHandler;

import com.example.todoapp.expception.IncorrectLoginCredential;
import com.example.todoapp.expception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<String> handleUserNotFound(Exception e){
        return ResponseEntity.status(404).body(e.getMessage());
    }
    @ExceptionHandler({IncorrectLoginCredential.class})
    public ResponseEntity<String> handleIncorrectCredential(Exception e){
        return ResponseEntity.status(401).body(e.getMessage());
    }
}
