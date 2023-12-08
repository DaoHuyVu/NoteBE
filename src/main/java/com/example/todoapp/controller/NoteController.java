package com.example.todoapp.controller;

import com.example.todoapp.dto.NoteDto;
import com.example.todoapp.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class NoteController {
    @Autowired
    private NoteService noteService;
    @PostMapping("/note")
    public ResponseEntity<?> addNote(@RequestBody @Valid NoteDto n){
        NoteDto note = noteService.addNote(n);
        return new ResponseEntity<>(note,HttpStatus.CREATED);
    }
    @GetMapping("/note")
    public ResponseEntity<?> getAllNotes(){
        List<NoteDto> notes = noteService.getAllNotes();
        return ResponseEntity.ok().body(notes);
    }
    @GetMapping("/note/{id}")
    public ResponseEntity<?> getNoteById(@PathVariable long id){
        NoteDto note = noteService.getNoteDtoById(id);
        return ResponseEntity.ok().body(note);
    }
    @DeleteMapping("/note/{id}")
    public ResponseEntity<?> deleteNoteById(@PathVariable long id){
        NoteDto note = noteService.deleteNote(id);
        return ResponseEntity.ok().body(note);
    }
    @PutMapping("/note/{id}")
    public ResponseEntity<?> updateNote(@RequestBody NoteDto n,@PathVariable long id){
        NoteDto note = noteService.updateNote(n,id);
        return ResponseEntity.ok().body(note);
    }
    @PatchMapping("/note/{id}")
    public ResponseEntity<?> patchNote(@RequestBody Map<String,Object> changes, @PathVariable Long id){
        NoteDto note = noteService.patchNote(changes,id);
        return ResponseEntity.ok().body(note);
    }
}
