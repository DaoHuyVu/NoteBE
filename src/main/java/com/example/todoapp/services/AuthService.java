package com.example.todoapp.services;

import com.example.todoapp.Constants;
import com.example.todoapp.exception.SessionExpiredException;
import com.example.todoapp.exception.UserExistException;
import com.example.todoapp.models.*;
import com.example.todoapp.repositories.RefreshTokenRepository;
import com.example.todoapp.repositories.RoleRepo;
import com.example.todoapp.repositories.UserRepository;
import com.example.todoapp.request.LoginRequest;
import com.example.todoapp.request.SignUpRequest;
import com.example.todoapp.response.AuthResponse;
import com.example.todoapp.response.Response;
import com.example.todoapp.security.jwt.JwtUtils;
import com.example.todoapp.security.services.UserDetailsImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Transactional(readOnly = true)
@SuppressWarnings("unused")
@Service
public class AuthService  {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private RedisCacheManager redisCacheManager;
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
    @Value(value = "${refreshTokenExpiration}")
    private Long refreshTokenExpiration;
    @Value(value = "${accessTokenExpiration}")
    private Long accessTokenExpiration;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Transactional
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
        String accessToken = jwtUtils.generateAccessTokenFromUserName(userDetails.getUsername(),userDetails.getId());
        String refreshToken = createRefreshToken(userDetails.getId(), userDetails.getUsername());
        return new AuthResponse(accessToken,refreshToken);
    }
    @Transactional
    public Response signUp(SignUpRequest signUpRequest){
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
        return new Response("Sign up successfully");
    }
    @Transactional
    public Response logout(String accessToken,String refreshToken){
        String accessTokenId = jwtUtils.getJTI(accessToken);
        Cache atCache = redisCacheManager.getCache(Constants.ACCESS_TOKEN);
        Long userId = jwtUtils.getUserIdFromJwt(refreshToken);
        if(atCache != null){
            atCache.put(accessTokenId,0);
            refreshTokenRepository.deleteByUserId(userId);
        }
        else{
            throw new RuntimeException("Access token cache is null,something wrong with redis");
        }
        return new Response("Logout successfully");
    }
    @Transactional
    private String createRefreshToken(Long id,String userName){
        Date now = new Date();
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setUser(userRepository.getReferenceById(id));
        refreshTokenEntity.setCreatedAt(now.getTime());
        refreshTokenEntity.setExpirationTime(now.getTime() + refreshTokenExpiration);
        refreshTokenRepository.save(refreshTokenEntity);
        return jwtUtils.generateRefreshTokenFromUserName(now,userName,id,refreshTokenEntity.getId());
    }
    @Transactional(noRollbackFor = SessionExpiredException.class)
    public AuthResponse refreshToken(String refreshToken){
        String userName = jwtUtils.getUserNameFromJwt(refreshToken);
        Long userId = jwtUtils.getUserIdFromJwt(refreshToken);
        if(isRefreshTokenValid(refreshToken,userId)){
            refreshTokenRepository.deleteById(Long.valueOf(jwtUtils.getJTI(refreshToken)));
            return new AuthResponse(
                    jwtUtils.generateAccessTokenFromUserName(userName,userId),
                    createRefreshToken(userId,userName)
            );
        }
        refreshTokenRepository.deleteByUserId(userId);
        throw new SessionExpiredException("Session expired, please make another login");
    }
    private Boolean isRefreshTokenValid(String token,Long userId){
        return refreshTokenRepository
                .findByUserIdAndId(userId,Long.valueOf(jwtUtils.getJTI(token))).isPresent();
    }
}
