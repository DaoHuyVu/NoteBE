package com.example.todoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto {
    long id;
    private String name;
    private String description;
    private boolean done;
    private String userName;

    public NoteDto(String name, String description, boolean done, String userName) {
        this.name = name;
        this.description = description;
        this.done = done;
        this.userName = userName;
    }
}
