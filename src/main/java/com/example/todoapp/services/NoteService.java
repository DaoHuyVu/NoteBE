package com.example.todoapp.services;

import com.example.todoapp.dto.NoteDto;
import com.example.todoapp.models.Note;
import com.example.todoapp.models.User;
import com.example.todoapp.repositories.NoteRepository;
import com.example.todoapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;
    public Note getNoteById(long id){
        return noteRepository.findById(id).orElse(null);
    }
    public List<NoteDto> getAllNotes(String userName){
        User user = userRepository.findUserByUserName(userName);
        return noteRepository.findNoteByUserId(user.getId())
                .stream()
                .map(note -> new NoteDto(
                        note.getId(),
                        note.getName(),
                        note.getDescription(),
                        note.getDone(),
                        note.getUser().getUserName()
                ))
                .collect(Collectors.toList());
    }
    public void addNote(NoteDto noteDto){
        User user = userRepository.findUserByUserName(noteDto.getUserName());
        noteRepository.save(new Note(noteDto.getName(),noteDto.getDescription(),noteDto.isDone(),user));
    }

    public void deleteNote(long id){
         noteRepository.deleteById(id);
    }
    public Note updateNote(Note note){
        Note existingNote = noteRepository.findById(note.getId()).orElseThrow();
        existingNote.setName(note.getName());
        existingNote.setDone(note.getDone());
        existingNote.setDescription(note.getDescription());
        return noteRepository.save(existingNote);
    }
}
