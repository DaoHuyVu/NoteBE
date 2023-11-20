package com.example.todoapp.request;

import lombok.Getter;

@Getter
public class SignUpRequest {
    private String userName;
    private String email;
    private String password;
}
