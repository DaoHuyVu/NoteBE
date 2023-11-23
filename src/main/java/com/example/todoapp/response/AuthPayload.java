package com.example.todoapp.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthPayload {
    private String access_token;
    private String refresh_token;
}
