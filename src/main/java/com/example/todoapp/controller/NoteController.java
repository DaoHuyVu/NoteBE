package com.example.todoapp.controller;

import com.example.todoapp.dto.NoteDto;
import com.example.todoapp.response.NotesPayload;
import com.example.todoapp.response.NotePayload;
import com.example.todoapp.response.Response;
import com.example.todoapp.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class NoteController {
    @Autowired
    private NoteService noteService;
    @PostMapping("/note")
    public ResponseEntity<Response<NotePayload>> addNote(@RequestBody NoteDto n){
        Response<NotePayload> note = noteService.addNote(n);
        return new ResponseEntity<>(note,HttpStatus.CREATED);
    }
    @GetMapping("/notes")
    public ResponseEntity<NotesPayload> getAllNotes(){
        NotesPayload notes = noteService.getAllNotes();
        return ResponseEntity.ok().body(notes);
    }
    @GetMapping("/notes/{id}")
    public ResponseEntity<NotePayload> getNoteById(@PathVariable long id){
        NotePayload note = noteService.getNoteDtoById(id);
        return ResponseEntity.ok().body(note);
    }
    @DeleteMapping("/notes/{id}")
    public ResponseEntity<Response<NotePayload>> deleteNoteById(@PathVariable long id){
        Response<NotePayload> response = noteService.deleteNote(id);
        return ResponseEntity.ok().body(response);
    }
    @PutMapping("/notes/{id}")
    public ResponseEntity<Response<NotePayload>> updateNote(@RequestBody NoteDto n,@PathVariable long id){
        Response<NotePayload> response = noteService.updateNote(n,id);
        return ResponseEntity.ok().body(response);
    }

}
