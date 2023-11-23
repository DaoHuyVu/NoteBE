package com.example.todoapp.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter

public class RefreshTokenRequest {
    @NotBlank
    private String refreshToken;
}
