package com.example.todoapp.controller;

import com.example.todoapp.dto.NoteDto;
import com.example.todoapp.models.Note;
import com.example.todoapp.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class NoteController {
    @Autowired
    private NoteService noteService;
    @PostMapping("api/v1/note")
    public ResponseEntity<String> addNote(@RequestBody NoteDto note){
        noteService.addNote(note);
        return new ResponseEntity<>("Add note successfully", HttpStatus.OK);
    }
    @GetMapping("api/v1/notes")
    public List<NoteDto> getAllNotes(@RequestBody String userName){
        return noteService.getAllNotes(userName);
    }
    @GetMapping("api/v1/notes/{id}")
    public Note getNoteById(@PathVariable long id){
        return noteService.getNoteById(id);
    }
    @DeleteMapping("api/v1/notes/{id}")
    public String deleteNoteById(@PathVariable long id){
        noteService.deleteNote(id);
        return "Note " + id + " deleted";
    }
    @PatchMapping("api/v1/notes/{id}")
    public Note updateNote(@RequestBody Note note){
        return noteService.updateNote(note);
    }

}
