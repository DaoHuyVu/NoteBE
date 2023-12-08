package com.example.todoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto implements Serializable {
    private long id;
    private String name;
    private String description;
    private Boolean done;

    public NoteDto(String name, String description, boolean done) {
        this.name = name;
        this.description = description;
        this.done = done;
    }
}
