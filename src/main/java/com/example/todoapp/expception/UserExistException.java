package com.example.todoapp.expception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserExistException extends RuntimeException{
    private String message;
    private int status;
}
