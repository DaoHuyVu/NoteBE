package com.example.todoapp.expception;

import lombok.AllArgsConstructor;

@AllArgsConstructor

public class IncorrectLoginCredential extends RuntimeException{
    private String message;
    private int status;
}
