package com.example.todoapp.exceptionHandler;

import com.example.todoapp.ErrorResponse;
import com.example.todoapp.expception.IncorrectLoginCredential;
import com.example.todoapp.expception.UserExistException;
import com.example.todoapp.expception.UserNotFoundException;
import org.hibernate.jdbc.Expectation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e){
        return new ResponseEntity<>(
                new ErrorResponse(new Date(),e.getMessage(),e.getStatus(),HttpStatus.NOT_FOUND)
                ,HttpStatus.NOT_FOUND
        );
    }
    @ExceptionHandler({IncorrectLoginCredential.class})
    public ResponseEntity<ErrorResponse> handleIncorrectCredential(IncorrectLoginCredential e){
        return new ResponseEntity<>(
                new ErrorResponse(new Date(),e.getMessage(),e.getStatus(),HttpStatus.UNAUTHORIZED)
        ,HttpStatus.UNAUTHORIZED
        );
    }
    @ExceptionHandler({UserExistException.class})
    public ResponseEntity<ErrorResponse> handleUserExist(UserExistException e){
        return new ResponseEntity<>(
                new ErrorResponse(new Date(),e.getMessage(),e.getStatus(),HttpStatus.CONFLICT),
                HttpStatus.CONFLICT
        );
    }
}
