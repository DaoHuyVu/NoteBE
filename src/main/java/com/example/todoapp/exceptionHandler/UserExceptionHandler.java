package com.example.todoapp.exceptionHandler;


import com.example.todoapp.exception.*;
import com.example.todoapp.response.ErrorResponse;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@RestControllerAdvice
@SuppressWarnings("unused")
public class UserExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({UserExistException.class})
    public ResponseEntity<ErrorResponse> handleUserExist(UserExistException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(),status),
                status
        );
    }
    @ExceptionHandler({
            AuthenticationException.class,
            TokenExpireException.class,
            TokenBlacklistedException.class,
            JwtException.class,
            SessionExpired.class
    })
    public ResponseEntity<ErrorResponse> handleAuthentication(Exception e){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(),status),
                status
        );
    }
    @ExceptionHandler({ ResourceNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleResourceNotFound(Exception e){
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(
                new ErrorResponse( e.getMessage(),status),
                status
        );
    }
    @ExceptionHandler({ AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleAccessDenied(Exception e){
        HttpStatus status = HttpStatus.FORBIDDEN;
        return new ResponseEntity<>(
                new ErrorResponse( e.getMessage(),status),
                status
        );
    }
    @ExceptionHandler({WrongFieldException.class})
    public ResponseEntity<ErrorResponse> handleWrongFieldException(Exception e){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(),status),
                status
        );
    }
}
