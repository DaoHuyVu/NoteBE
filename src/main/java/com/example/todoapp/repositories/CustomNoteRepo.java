package com.example.todoapp.repositories;

import com.example.todoapp.models.Note;

import java.util.List;

public interface CustomNoteRepo {
    List<Note> findNoteByUserId(Long id);
}
