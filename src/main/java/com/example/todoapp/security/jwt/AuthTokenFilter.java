package com.example.todoapp.security.jwt;

import com.example.todoapp.security.services.UserDetailsServiceImp;
import com.example.todoapp.services.NoteService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserDetailsServiceImp userDetailsServiceImp;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try{
            String jwt;
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            logger.error(authHeader);
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                filterChain.doFilter(request,response);
                return;
            }
            jwt = authHeader.substring("Bearer ".length());
            if(jwtUtils.validateToken(jwt)){
                String userName = jwtUtils.getUserNameFromJwt(jwt);
                UserDetails userDetail = userDetailsServiceImp.loadUserByUsername(userName);
                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetail,null,userDetail.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                logger.info(authenticationToken.toString());
            }
        }catch (Exception e){
            logger.error("Cannot set authentication : {}",e.getMessage());
        }
        filterChain.doFilter(request,response);
    }
}
