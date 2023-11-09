package com.example.todoapp.controller;

import com.example.todoapp.dto.LoginDto;
import com.example.todoapp.dto.SignUpDto;
import com.example.todoapp.dto.UserDto;

import com.example.todoapp.models.Note;

import com.example.todoapp.models.User;
import com.example.todoapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@RestController
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/api/v1/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        String userName = userService.login(loginDto);
        return new ResponseEntity<>(userName, HttpStatus.OK);


    }
    @PostMapping("/api/v1/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpDto signUpDto){
        String userName = userService.signUp(signUpDto);
        return new ResponseEntity<>(userName,HttpStatus.OK);
    }
}
