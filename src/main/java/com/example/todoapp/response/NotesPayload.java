package com.example.todoapp.response;

import com.example.todoapp.dto.NoteDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NotesPayload {
    private List<NoteDto> data;
}
