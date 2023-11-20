package com.example.todoapp.exceptionHandler;


import com.example.todoapp.response.MessageResponse;
import com.example.todoapp.exception.IncorrectLoginCredential;
import com.example.todoapp.exception.UserExistException;
import com.example.todoapp.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<MessageResponse> handleUserNotFound(UserNotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(new MessageResponse(new Date(),"User not found"),status);
    }

    @ExceptionHandler({IncorrectLoginCredential.class})
    public ResponseEntity<MessageResponse> handleIncorrectCredential(IncorrectLoginCredential e) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return new ResponseEntity<>(
                new MessageResponse(new Date(), e.getMessage()),
                status
        );
    }

    @ExceptionHandler({UserExistException.class})
    public ResponseEntity<MessageResponse> handleUserExist(UserExistException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        return new ResponseEntity<>(
                new MessageResponse(new Date(), e.getMessage()),
                status
        );
    }
    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<MessageResponse> handleAuthentication(AuthenticationException e){
        HttpStatus status = HttpStatus.FORBIDDEN;
        return new ResponseEntity<>(
                new MessageResponse(new Date(),e.getMessage()),
                status
        );
    }
}
