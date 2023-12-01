package com.example.todoapp.config;

import com.example.todoapp.security.jwt.AccessDeniedExceptionFilter;
import com.example.todoapp.security.jwt.AuthTokenFilter;
import com.example.todoapp.security.jwt.JwtUtils;
import com.example.todoapp.security.services.UserDetailsServiceImp;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.event.spi.DeleteEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

@Configuration
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true
)
public class SecurityConfig {
    @Bean
    public ExceptionHandlerExceptionResolver exceptionResolver(){
        return new ExceptionHandlerExceptionResolver();
    }
    @Bean
    public JwtUtils jwtUtils(){
        return new JwtUtils();
    }

    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;
    @Autowired
    @Qualifier("authEntryPointJwt")
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsServiceImp);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }
    @Bean
    public FilterRegistrationBean<AuthTokenFilter> authTokenRegistrationBean(){
        FilterRegistrationBean<AuthTokenFilter> registrationBean = new FilterRegistrationBean<>(authTokenFilter());
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AccessDeniedExceptionFilter> accessDeniedExceptionFilterRegistrationBean() {
        FilterRegistrationBean<AccessDeniedExceptionFilter> registrationBean = new FilterRegistrationBean<>(accessDeniedExceptionFilter());
        registrationBean.setEnabled(false);
        return registrationBean;
    }
    @Bean
    public AccessDeniedExceptionFilter accessDeniedExceptionFilter(){
        return new AccessDeniedExceptionFilter();
    }
    @Bean
    public AuthTokenFilter authTokenFilter(){
        return new AuthTokenFilter();
    }
    @Bean
    @Order(1)
    public SecurityFilterChain authFilterChain(HttpSecurity http) throws Exception{
        http
                .securityMatcher("/api/auth/**")
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll())
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager());
        return http.build();
    }
    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager())
                .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(accessDeniedExceptionFilter(), AuthorizationFilter.class);
        return http.build();
    }
}
