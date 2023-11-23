package com.example.todoapp.response;

import com.example.todoapp.dto.NoteDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Getter
public class NotePayload {
    private NoteDto data;
}
