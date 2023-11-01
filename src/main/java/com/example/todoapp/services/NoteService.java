package com.example.todoapp.services;

import com.example.todoapp.dto.NoteDto;
import com.example.todoapp.models.Note;
import com.example.todoapp.models.User;
import com.example.todoapp.repositories.NoteRepository;
import com.example.todoapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;
    public Note getNoteById(long id){
        return noteRepository.findById(id).orElse(null);
    }
    public List<Note> getAllNotes(){
        return noteRepository.findAll();
    }
    public void addNote(NoteDto noteDto){
        User user = userRepository.findUserByUserName(noteDto.getUser());
        noteRepository.save(new Note(noteDto.getName(),noteDto.getDescription(),false,user));
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
