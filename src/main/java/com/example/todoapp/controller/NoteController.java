package com.example.todoapp.controller;

import com.example.todoapp.dto.NoteDto;
import com.example.todoapp.models.Note;
import com.example.todoapp.services.NoteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
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
    private final ObjectMapper mapper = new ObjectMapper();
    @PostMapping("/note")
    public ResponseEntity<?> addNote(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("createdAt") String createdAt,
            @RequestParam("done") String done
    ){
        NoteDto note = noteService.addNote(name,description,createdAt,done);
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
    public ResponseEntity<?> deleteNoteById(@PathVariable String id){
        NoteDto note = noteService.deleteNote(Long.parseLong(id));
        return ResponseEntity.ok().body(note);
    }
    @PutMapping("/note/{id}")
    public ResponseEntity<?> updateNote(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @PathVariable long id){
        NoteDto note = noteService.updateNote(name,description,id);
        return ResponseEntity.ok().body(note);
    }
    @PatchMapping("/note/{id}")
    public ResponseEntity<?> patchNote(
            @PathVariable Long id,
            @RequestParam String changes) throws JsonProcessingException {
        Map<String,String> fields = mapper.readValue(changes, new TypeReference<>() {});
        NoteDto note = noteService.patchNote(fields,id);
        return ResponseEntity.ok().body(note);
    }
}
