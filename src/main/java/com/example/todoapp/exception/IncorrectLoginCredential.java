package com.example.todoapp.exception;

import lombok.AllArgsConstructor;

import lombok.Getter;

@AllArgsConstructor
@Getter


public class IncorrectLoginCredential extends RuntimeException{
    private String message;
}
