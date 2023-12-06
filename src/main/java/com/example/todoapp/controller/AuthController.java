package com.example.todoapp.controller;

import com.example.todoapp.request.LoginRequest;
import com.example.todoapp.request.RefreshTokenRequest;
import com.example.todoapp.request.SignUpRequest;

import com.example.todoapp.response.AuthResponse;
import com.example.todoapp.response.Response;
import com.example.todoapp.services.AuthService;
import com.example.todoapp.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestHeader(name = "Accept-Version",defaultValue = "1.0.0",required = false) String version,
            @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(
            @RequestBody SignUpRequest signUpRequest,
            @RequestHeader(name = "Accept-Version",defaultValue = "1.0.0",required = false) String version){
        AuthResponse response = authService.signUp(signUpRequest);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/refreshToken")
    public ResponseEntity<AuthResponse> getRefreshToken(@RequestBody @Valid RefreshTokenRequest request){
        AuthResponse response = refreshTokenService.getRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok().body(response);
    }

}
