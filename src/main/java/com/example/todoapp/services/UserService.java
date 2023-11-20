package com.example.todoapp.services;

import com.example.todoapp.models.ERole;
import com.example.todoapp.models.Role;
import com.example.todoapp.repositories.RoleRepo;
import com.example.todoapp.request.SignUpRequest;
import com.example.todoapp.exception.UserExistException;
import com.example.todoapp.exception.UserNotFoundException;
import com.example.todoapp.request.LoginRequest;
import com.example.todoapp.models.User;
import com.example.todoapp.repositories.NoteRepository;
import com.example.todoapp.repositories.UserRepository;
import com.example.todoapp.response.AuthResponse;
import com.example.todoapp.security.jwt.JwtUtils;
import com.example.todoapp.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class UserService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepo roleRepo;
    public AuthResponse login(LoginRequest loginRequest){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserName(),
                        loginRequest.getPassword()
                ));
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String token = jwtUtils.generateTokenFromUserName(userDetails.getUsername());
        return new AuthResponse(token);
    }
    public AuthResponse signUp(SignUpRequest signUpRequest){
        if(userRepository.existsByUserName(signUpRequest.getUserName()))
            throw new UserExistException("User already exist");

        if(userRepository.existsByEmail(signUpRequest.getEmail()))
            throw new UserExistException("This email has already been used");
        Set<Role> roles = new HashSet<>();
        Role role = roleRepo.findByRole(ERole.USER);
        roles.add(role);
        userRepository.save(new User(
                signUpRequest.getEmail(),
                signUpRequest.getUserName(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                roles
        ));
        String token = jwtUtils.generateTokenFromUserName(signUpRequest.getUserName());
        return new AuthResponse(token);
    }

}
