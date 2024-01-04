package com.example.todoapp.services;

import com.example.todoapp.models.ERole;
import com.example.todoapp.models.RefreshToken;
import com.example.todoapp.models.Role;
import com.example.todoapp.repositories.RoleRepo;
import com.example.todoapp.request.SignUpRequest;
import com.example.todoapp.exception.UserExistException;
import com.example.todoapp.request.LoginRequest;
import com.example.todoapp.models.User;
import com.example.todoapp.repositories.UserRepository;
import com.example.todoapp.response.AuthResponse;
import com.example.todoapp.response.Response;
import com.example.todoapp.security.jwt.JwtUtils;
import com.example.todoapp.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class AuthService extends AccessDeniedHandlerImpl {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenService refreshTokenService;
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
        String accessToken = jwtUtils.generateTokenFromUserName(userDetails.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return new AuthResponse(accessToken,refreshToken.getToken());
    }
    public Response signUp(SignUpRequest signUpRequest){
        if(userRepository.existsByUserName(signUpRequest.getUserName()))
            throw new UserExistException("User already exist");

        if(userRepository.existsByEmail(signUpRequest.getEmail()))
            throw new UserExistException("This email has already been used");
        Set<Role> roles = new HashSet<>();
        Role role = roleRepo.findByRole(ERole.USER);
        roles.add(role);
        User user = userRepository.save(new User(
                signUpRequest.getEmail(),
                signUpRequest.getUserName(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                roles
        ));
        return new Response("Sign up successfully");
    }

}
