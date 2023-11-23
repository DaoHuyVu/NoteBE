package com.example.todoapp.request;

import lombok.Data;
import lombok.Getter;

@Getter
public class LoginRequest {
    private String userName;
    private String password;
}
