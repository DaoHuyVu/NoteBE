package com.example.todoapp.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-YYYY hh:mm:ss")
    private Date timeStamp;
    private String message;
    private HttpStatus status;
}
