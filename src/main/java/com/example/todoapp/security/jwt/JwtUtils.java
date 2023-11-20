package com.example.todoapp.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${jwtSecret}")
    private String jwtSecret;
    @Value(("${jwtExpiration}"))
    private long jwtExpiration;
    private SecretKey key(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    public String getUserNameFromJwt(String token){
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    public boolean validateToken(String token){
        try{
            Jwts.parser().verifyWith(key()).build().parse(token);
            return true;
        }catch(MalformedJwtException e){
            logger.error("Invalid jwt token: {}",e.getMessage());
        }catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
    public String generateTokenFromUserName(String userName){
        return Jwts
                .builder()
                .subject(userName)
                .expiration(new Date(new Date().getTime() + jwtExpiration))
                .issuedAt(new Date())
                .signWith(key())
                .compact();
    }
}
