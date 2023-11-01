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
public class NoteController {
    @Autowired
    private NoteService noteService;
    @PostMapping("/addNote")
    public ResponseEntity<String> addNote(@RequestBody NoteDto note){
       return new ResponseEntity<>("Add note successfully", HttpStatus.OK);
    }
    @GetMapping("/notes")
    public List<Note> getAllNotes(){
        return noteService.getAllNotes();
    }
    @GetMapping("/notes/{id}")
    public Note getNoteById(@PathVariable long id){
        return noteService.getNoteById(id);
    }
    @DeleteMapping("notes/{id}")
    @ResponseBody
    public String deleteNoteById(@PathVariable long id){
        noteService.deleteNote(id);
        return "Note " + id + " deleted";
    }
    @PatchMapping("notes/{id}")
    public Note updateNote(@RequestBody Note note){
        return noteService.updateNote(note);
    }

}
