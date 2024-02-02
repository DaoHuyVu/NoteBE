package com.example.todoapp.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SessionExpired extends RuntimeException{
    public SessionExpired(String message){
        super(message);
    }
}
