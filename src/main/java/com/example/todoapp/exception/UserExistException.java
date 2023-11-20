package com.example.todoapp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserExistException extends RuntimeException{
    private String message;
}
