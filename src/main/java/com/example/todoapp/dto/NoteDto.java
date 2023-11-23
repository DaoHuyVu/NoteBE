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
    private long id;
    private String name;
    private String description;
    private boolean done;

    public NoteDto(String name, String description, boolean done) {
        this.name = name;
        this.description = description;
        this.done = done;
    }
}
