package com.example.todoapp.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class ExceptionHandler {
    private static final ObjectMapper mapper = new ObjectMapper();
    public static void handleJwtException(String message, HttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(message);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED);
        OutputStream os = response.getOutputStream();
        mapper.writeValue(os,errorResponse);
        os.flush();
    }
    public static void handleBadRequestException(String message,HttpServletResponse response) throws IOException {
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
