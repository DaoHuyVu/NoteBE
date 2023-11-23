package com.example.todoapp.services;

import com.example.todoapp.dto.NoteDto;
import com.example.todoapp.exception.ResourceNotFoundException;
import com.example.todoapp.models.Note;
import com.example.todoapp.models.User;
import com.example.todoapp.repositories.NoteRepository;
import com.example.todoapp.repositories.UserRepository;
import com.example.todoapp.response.NotesPayload;
import com.example.todoapp.response.NotePayload;
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

    public NotePayload getNoteDtoById(long id) {
        UserDetails userDetails = getUserDetail();
            NoteDto note = noteRepository.findByIdAndUsername(id, userDetails.getUsername());
            if(note == null) throw new ResourceNotFoundException("Note not found");
        return new NotePayload(note);
    }
    public NotesPayload getAllNotes(){
        UserDetails userDetails = getUserDetail();
        List<NoteDto> notes = noteRepository.findByUsername(userDetails.getUsername());
        return new NotesPayload(notes);
    }
    public Response<NotePayload> addNote(NoteDto noteDto){
        UserDetailsImpl userDetails = (UserDetailsImpl) getUserDetail();
        User user = userRepository.getReferenceById(userDetails.getId());
        noteRepository.save(new Note(noteDto.getName(),noteDto.getDescription(),noteDto.isDone(),user));
        return new Response<>(new Date(), HttpStatus.CREATED,"Add note successfully",null);
    }

    public Response<NotePayload> deleteNote(long id){
         UserDetails userDetails = getUserDetail();
         NoteDto note = noteRepository.findByIdAndUsername(id,userDetails.getUsername());
         if(note == null) throw new ResourceNotFoundException("Note not found");
         noteRepository.deleteById(id);
         return new Response<>(new Date(),HttpStatus.OK,"Delete successfully",null);
    }
    public Response<NotePayload> updateNote(NoteDto n,long id){
        UserDetails userDetails = getUserDetail();
        NoteDto note = noteRepository.findByIdAndUsername(id,userDetails.getUsername());
        if(note == null) throw new ResourceNotFoundException("Note not found");
        note.setName(n.getName());
        note.setDone(n.isDone());
        note.setDescription(n.getDescription());
        noteRepository.updateNote(note);
        return new Response<>(new Date(),HttpStatus.OK,"Update successfully",null);
    }
    private UserDetails getUserDetail(){
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
