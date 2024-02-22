package com.example.todoapp.security.jwt;

import com.example.todoapp.Constants;
import com.example.todoapp.exception.TokenBlacklistedException;
import io.hypersistence.tsid.TSID;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;

import javax.crypto.SecretKey;
import java.util.Date;

@SuppressWarnings("unused")
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${jwtSecret}")
    private String jwtSecret;
    @Autowired
    private RedisCacheManager redisCacheManager;
    @Value("${accessTokenExpiration}")
    private long accessTokenExpiration;

    @Value("${refreshTokenExpiration}")
    private long refreshTokenExpiration;
    private SecretKey key(){
        byte[] keyByte = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyByte);
    }
    public String getUserNameFromJwt(String token){
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    public Long getUserIdFromJwt(String token){
        return ((Integer) Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("user_id")).longValue();
    }
    public boolean isTokenValid(String token){
        if(isTokenInBlackList(token))
            throw new TokenBlacklistedException("This token was blacklisted");
        Jwts.parser().verifyWith(key()).build().parseSignedClaims(token);
        return true;
    }
    public String generateAccessTokenFromUserName(String userName, Long userId){
        return Jwts
                .builder()
                .header()
                .type("JWT")
                .and()
                .subject(userName)
                .claim("user_id",userId)
                .expiration(new Date(new Date().getTime() + accessTokenExpiration))
                .issuedAt(new Date())
                .signWith(key())
                .id(String.valueOf(TSID.fast().toLong()))
                .compact();
    }
    public String generateRefreshTokenFromUserName(Date now,String userName,Long userId,Long jti){
        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .issuedAt(now)
                .expiration(new Date(now.getTime() + refreshTokenExpiration))
                .claim("user_id",userId)
                .id(String.valueOf(jti))
                .signWith(key())
                .subject(userName)
                .compact();
    }
    public String getJTI(String token){
        return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload().getId();
    }
    public Boolean isTokenInBlackList(String token){
        String id = getJTI(token);
        Cache cache = redisCacheManager.getCache(Constants.ACCESS_TOKEN);
        if(cache != null){
            return cache.get(id) != null;
        }
        return false;
    }
    public void getTokenExpiration(String token){
        Jwts.parser().verifyWith(key()).build()
                .parseSignedClaims(token).getPayload().getExpiration();
    }
}
