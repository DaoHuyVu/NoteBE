package com.example.todoapp.controller;

import com.example.todoapp.exception.TokenExpireException;
import com.example.todoapp.models.RefreshToken;
import com.example.todoapp.request.LoginRequest;
import com.example.todoapp.request.RefreshTokenRequest;
import com.example.todoapp.request.SignUpRequest;

import com.example.todoapp.response.AuthPayload;
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
    public ResponseEntity<Response<AuthPayload>> login(
            @RequestHeader(name = "Accept-Version",defaultValue = "1.0.0",required = false) String version,
            @RequestBody LoginRequest loginRequest) {
        Response<AuthPayload> response = authService.login(loginRequest);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<Response<AuthPayload>> signUp(
            @RequestBody SignUpRequest signUpRequest,
            @RequestHeader(name = "Accept-Version",defaultValue = "1.0.0",required = false) String version){
        Response<AuthPayload> response = authService.signUp(signUpRequest);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/refreshToken")
    public ResponseEntity<Response<AuthPayload>> getRefreshToken(@RequestBody @Valid RefreshTokenRequest request){
        Response<AuthPayload> response = refreshTokenService.getRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok().body(response);
    }

}
