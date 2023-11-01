package com.example.todoapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteDto {
    private String name;
    private String description;
    private boolean done;
    private String user;
}
