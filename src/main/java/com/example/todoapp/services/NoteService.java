package com.example.todoapp.services;

import com.example.todoapp.dto.NoteDto;
import com.example.todoapp.exception.ResourceNotFoundException;
import com.example.todoapp.models.Note;
import com.example.todoapp.models.User;
import com.example.todoapp.repositories.NoteRepository;
import com.example.todoapp.repositories.UserRepository;
import com.example.todoapp.response.Response;
import com.example.todoapp.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
@Service

public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    private static final Logger logger = LoggerFactory.getLogger(NoteService.class);

    @Autowired
    private UserRepository userRepository;

    public NoteDto getNoteDtoById(long id) {
        UserDetails userDetails = getUserDetail();
            NoteDto note = noteRepository.findByIdAndUsername(id, userDetails.getUsername());
            if(note == null) throw new ResourceNotFoundException("Note not found");
        return note;
    }
    public List<NoteDto> getAllNotes(){
        UserDetails userDetails = getUserDetail();
        return noteRepository.findByUsername(userDetails.getUsername());
    }
    public Response addNote(NoteDto noteDto){
        UserDetailsImpl userDetails = (UserDetailsImpl) getUserDetail();
        User user = userRepository.getReferenceById(userDetails.getId());
        noteRepository.save(new Note(noteDto.getName(),noteDto.getDescription(),noteDto.isDone(),user));
        return new Response("Add note successfully");
    }

    public Response deleteNote(long id){
         UserDetails userDetails = getUserDetail();
         NoteDto note = noteRepository.findByIdAndUsername(id,userDetails.getUsername());
         if(note == null) throw new ResourceNotFoundException("Note not found");
         noteRepository.deleteById(id);
         return new Response("Delete successfully");
    }
    public Response updateNote(NoteDto n,long id){
        UserDetails userDetails = getUserDetail();
        NoteDto note = noteRepository.findByIdAndUsername(id,userDetails.getUsername());
        if(note == null) throw new ResourceNotFoundException("Note not found");
        note.setName(n.getName());
        note.setDone(n.isDone());
        note.setDescription(n.getDescription());
        noteRepository.updateNote(note);
        return new Response("Update successfully");
    }
    private UserDetails getUserDetail(){
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
