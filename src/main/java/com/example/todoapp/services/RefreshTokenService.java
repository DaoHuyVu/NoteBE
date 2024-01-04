package com.example.todoapp.services;

import com.example.todoapp.exception.ResourceNotFoundException;
import com.example.todoapp.exception.TokenExpireException;
import com.example.todoapp.models.RefreshToken;
import com.example.todoapp.models.User;
import com.example.todoapp.repositories.RefreshTokenRepo;
import com.example.todoapp.repositories.UserRepository;
import com.example.todoapp.response.AuthResponse;
import com.example.todoapp.response.Response;
import com.example.todoapp.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class RefreshTokenService {
    @Value(value = "${refreshTokenExpiration}")
    private long refreshTokenExpiration;
    @Autowired
    private RefreshTokenRepo refreshTokenRepo;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;
    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepo.findByToken(token);
    }
    public RefreshToken createRefreshToken(long id){
        RefreshToken token;
        Optional<RefreshToken> refreshToken = refreshTokenRepo.findById(id);
        if(refreshToken.isPresent()){
            token = refreshToken.get();
        }
        else{
            token = new RefreshToken();
            User user = userRepository.findById(id).orElseThrow(null);
            token.setExpirationDate(Instant.now().plusMillis(refreshTokenExpiration));
            token.setUser(user);
            token.setToken(jwtUtils.generateRefreshTokenFromUserName(user.getUserName()));
            refreshTokenRepo.save(token);
        }
        return token;
    }
    public AuthResponse getNewAccessToken(String refreshToken){
        return findByToken(refreshToken)
                .map(this::verifyTokenExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtUtils.generateTokenFromUserName(user.getUserName());
                    return new AuthResponse(accessToken,refreshToken);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token is not in database"));
    }
    RefreshToken verifyTokenExpiration(RefreshToken token){
        if(Instant.now().compareTo(token.getExpirationDate()) > 0){
            refreshTokenRepo.delete(token);
            throw new TokenExpireException("Refresh token expired, please make a new sign up request!");
        }
        return token;
    }

}
