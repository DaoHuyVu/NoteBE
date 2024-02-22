package com.example.todoapp.exceptionHandler;


import com.example.todoapp.exception.*;
import com.example.todoapp.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpHeaders;
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
    @ExceptionHandler({
            UserExistException.class
    })
    public ResponseEntity<ErrorResponse> handleConflictException(Exception e) {
        HttpStatus status = HttpStatus.CONFLICT;
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(),status),
                status
        );
    }
    @ExceptionHandler({
            SessionExpiredException.class,
            JwtException.class
    })
    public ResponseEntity<ErrorResponse> handleSessionExpiredException(Exception e){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        HttpHeaders header = new HttpHeaders();
        header.add("X-Auth-Status","Expired");
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(),status),
                header,
                status
        );
    }
    @ExceptionHandler({
            AuthenticationException.class,
            TokenBlacklistedException.class,
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
                new ErrorResponse(e.getMessage(),status),
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
