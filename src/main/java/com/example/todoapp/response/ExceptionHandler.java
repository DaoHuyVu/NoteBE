package com.example.todoapp.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class ExceptionHandler {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
    public static void handleJwtException(String message, HttpServletResponse response) throws IOException {
        logger.error(message);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(message);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("X-Auth-Status","Expired");
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED);
        OutputStream os = response.getOutputStream();
        mapper.writeValue(os,errorResponse);
        os.flush();
    }
    public static void handleBadRequestException(String message,HttpServletResponse response) throws IOException {
        logger.error(message);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(message);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        OutputStream os = response.getOutputStream();
        mapper.writeValue(os,errorResponse);
        os.flush();
    }
}
