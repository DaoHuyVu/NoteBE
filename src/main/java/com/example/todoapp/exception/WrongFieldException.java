package com.example.todoapp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class WrongFieldException extends RuntimeException{
    private String message;
}
