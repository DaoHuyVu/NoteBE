package com.example.todoapp.controller;

import com.example.todoapp.models.Note;
import com.example.todoapp.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NoteController {
    @Autowired
    private NoteService noteService;
    @PostMapping("/addNote")
    public Note addNote(@RequestBody Note note){
       return  noteService.addNote(note);
    }
    @PostMapping("/addNotes")
    public List<Note> addNotes(@RequestBody List<Note> notes){
        return noteService.addNotes(notes);
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
