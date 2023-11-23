package com.example.todoapp.exception;



public class TokenExpireException extends RuntimeException{
    public TokenExpireException(String message){
        super(message);
    }
}
