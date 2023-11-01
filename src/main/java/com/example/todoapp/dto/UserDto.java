package com.example.todoapp.dto;

import com.example.todoapp.models.Note;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@AllArgsConstructor
@Data
public class UserDto {
    private String userName;
    private List<Note> notes;

}
