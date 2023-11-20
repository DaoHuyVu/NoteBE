package com.example.todoapp.controller;

import com.example.todoapp.request.LoginRequest;
import com.example.todoapp.request.SignUpRequest;

import com.example.todoapp.response.AuthResponse;
import com.example.todoapp.response.MessageResponse;
import com.example.todoapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @PostMapping("/v1/login")
    public ResponseEntity<AuthResponse> login(@RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) @RequestBody LoginRequest loginRequest) {
        AuthResponse loginResponse = userService.login(loginRequest);
        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/v1/signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody SignUpRequest signUpRequest){
        AuthResponse response  = userService.signUp(signUpRequest);
        return ResponseEntity.ok().body(response);
    }
}
