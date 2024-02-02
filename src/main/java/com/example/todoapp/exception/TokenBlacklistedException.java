package com.example.todoapp.exception;

import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class TokenBlacklistedException extends JwtException {
    public TokenBlacklistedException(String message) {
        super(message);
    }
}
