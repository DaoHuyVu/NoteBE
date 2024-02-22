package com.example.todoapp.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SessionExpiredException extends RuntimeException{
    public SessionExpiredException(String message){
        super(message);
    }
}
