package com.example.todoapp.services;

import com.example.todoapp.models.Note;
import com.example.todoapp.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    public Note getNoteById(long id){
        return noteRepository.findById(id).orElse(null);
    }
    public List<Note> getAllNotes(){
        return noteRepository.findAll();
    }
    public Note addNote(Note note){
        return noteRepository.save(note);
    }
    public List<Note> addNotes(List<Note> notes){
        return noteRepository.saveAll(notes);
    }
    public void deleteNote(long id){
         noteRepository.deleteById(id);
    }
    public Note updateNote(Note note){
        Note existingNote = noteRepository.findById(note.getId()).orElse(null);
        assert existingNote != null;
        existingNote.setName(note.getName());
        existingNote.setDone(note.getDone());
        existingNote.setDescription(note.getDescription());
        return noteRepository.save(existingNote);
    }
}
