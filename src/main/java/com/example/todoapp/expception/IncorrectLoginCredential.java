package com.example.todoapp.expception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class IncorrectLoginCredential extends RuntimeException{
    private String message;
    private int status;
}
