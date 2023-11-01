package com.example.todoapp.services;

import com.example.todoapp.dto.SignUpDto;
import com.example.todoapp.expception.IncorrectLoginCredential;
import com.example.todoapp.expception.UserExistException;
import com.example.todoapp.expception.UserNotFoundException;
import com.example.todoapp.dto.LoginDto;
import com.example.todoapp.dto.UserDto;
import com.example.todoapp.models.Note;
import com.example.todoapp.models.User;
import com.example.todoapp.repositories.UserRepository;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCrypt;

    public UserDto login(LoginDto loginDto){
        User user = userRepository.findUserByUserName(loginDto.getUserName());
        if(user == null){
            user = userRepository.findUserByEmail(loginDto.getUserName());
            if(user == null)
                throw new UserNotFoundException("User not found",404);
        }
        if(bCrypt.matches(loginDto.getPassword(),user.getPassword())){
            return new UserDto(user.getUserName(),userRepository.findNoteById(user.getId()));
        }
        throw new IncorrectLoginCredential("Username or password is incorrect",401);
    }
    public String signUp(SignUpDto signUpDto){
        User user = userRepository.findUserByUserName(signUpDto.getUserName());
        if(user != null) throw new UserExistException("User already exist",409);

        user = userRepository.findUserByEmail(signUpDto.getEmail());
        if(user != null) throw new UserExistException("This email was already been used",409);

        userRepository.save(new User(
                signUpDto.getEmail(),
                signUpDto.getUserName(),
                bCrypt.encode(signUpDto.getPassword())
        ));
        return signUpDto.getUserName();
    }
}
