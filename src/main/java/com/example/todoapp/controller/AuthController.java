package com.example.todoapp.controller;

import com.example.todoapp.request.LoginRequest;
import com.example.todoapp.request.SignUpRequest;

import com.example.todoapp.response.AuthResponse;
import com.example.todoapp.response.Response;
import com.example.todoapp.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@SuppressWarnings("unused")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(
            @RequestBody SignUpRequest signUpRequest)
    {
        Response response = authService.signUp(signUpRequest);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/refreshToken")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestParam String refreshToken
    ){
        AuthResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/logout")
        public ResponseEntity<?> logout(
                @RequestParam String refreshToken,
                @RequestParam String accessToken
    ){
        return ResponseEntity.ok().body(authService.logout(accessToken,refreshToken));
    }
}