package com.example.todoapp.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
}
