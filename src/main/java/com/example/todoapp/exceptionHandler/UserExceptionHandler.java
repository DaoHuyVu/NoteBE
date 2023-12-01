package com.example.todoapp.exceptionHandler;


import com.example.todoapp.exception.ResourceNotFoundException;
import com.example.todoapp.exception.TokenExpireException;
import com.example.todoapp.exception.UserExistException;
import com.example.todoapp.response.ErrorResponse;
import com.example.todoapp.response.NotePayload;
import com.example.todoapp.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({UserExistException.class})
    public ResponseEntity<ErrorResponse> handleUserExist(UserExistException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        return new ResponseEntity<>(
                new ErrorResponse(new Date(),e.getMessage(),status),
                status
        );
    }
    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleAuthentication(Exception e){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return new ResponseEntity<>(
                new ErrorResponse(new Date(), e.getMessage(),status),
                status
        );
    }
    @ExceptionHandler({ ResourceNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleResourceNotFound(Exception e){
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(
                new ErrorResponse(new Date(), e.getMessage(),status),
                status
        );
    }
    @ExceptionHandler({ TokenExpireException.class})
    public ResponseEntity<ErrorResponse> handleTokenExpire(Exception e){
        HttpStatus status = HttpStatus.FORBIDDEN;
        return new ResponseEntity<>(
                new ErrorResponse(new Date(), e.getMessage(),status),
                status
        );
    }
    @ExceptionHandler({ AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleAccessDenied(Exception e){
        HttpStatus status = HttpStatus.FORBIDDEN;
        return new ResponseEntity<>(
                new ErrorResponse(new Date(), e.getMessage(),status),
                status
        );
    }
}
