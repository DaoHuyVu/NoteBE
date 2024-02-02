package com.example.todoapp.exception;


import io.jsonwebtoken.JwtException;

public class TokenExpireException extends JwtException {
    public TokenExpireException(String message){
        super(message);
    }
}
