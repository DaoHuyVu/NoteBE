package com.example.todoapp.controller;

import com.example.todoapp.dto.NoteDto;
import com.example.todoapp.response.Response;
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
    @PostMapping("/note")
    public ResponseEntity<Response> addNote(@RequestBody NoteDto n){
        Response note = noteService.addNote(n);
        return new ResponseEntity<>(note,HttpStatus.CREATED);
    }
    @GetMapping("/notes")
    public ResponseEntity<?> getAllNotes(){
        List<NoteDto> notes = noteService.getAllNotes();
        return ResponseEntity.ok().body(notes);
    }
    @GetMapping("/notes/{id}")
    public ResponseEntity<?> getNoteById(@PathVariable long id){
        NoteDto note = noteService.getNoteDtoById(id);
        return ResponseEntity.ok().body(note);
    }
    @DeleteMapping("/notes/{id}")
    public ResponseEntity<Response> deleteNoteById(@PathVariable long id){
        Response response = noteService.deleteNote(id);
        return ResponseEntity.ok().body(response);
    }
    @PutMapping("/notes/{id}")
    public ResponseEntity<Response> updateNote(@RequestBody NoteDto n,@PathVariable long id){
        Response response = noteService.updateNote(n,id);
        return ResponseEntity.ok().body(response);
    }

}
