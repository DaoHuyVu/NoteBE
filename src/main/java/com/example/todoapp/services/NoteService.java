package com.example.todoapp.services;

import com.example.todoapp.dto.NoteDto;
import com.example.todoapp.exception.ResourceNotFoundException;
import com.example.todoapp.exception.WrongFieldException;
import com.example.todoapp.models.Note;
import com.example.todoapp.models.User;
import com.example.todoapp.repositories.NoteRepository;
import com.example.todoapp.repositories.UserRepository;
import com.example.todoapp.response.Response;
import com.example.todoapp.security.services.UserDetailsImpl;
import jakarta.persistence.EntityManager;
import org.apache.el.util.ReflectionUtil;
import org.aspectj.util.Reflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.NumberUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public NoteDto addNote(NoteDto noteDto){
        UserDetailsImpl userDetails = (UserDetailsImpl) getUserDetail();
        User user = userRepository.getReferenceById(userDetails.getId());
        Note note = noteRepository.save(
                new Note(
                        noteDto.getName().trim(),
                        noteDto.getDescription().trim(),
                        noteDto.getDone(),
                        user));
        return note.toNoteDto();
    }

    public NoteDto deleteNote(long id){
         UserDetails userDetails = getUserDetail();
         NoteDto note = noteRepository.findByIdAndUsername(id,userDetails.getUsername());
         if(note == null) throw new ResourceNotFoundException("Note not found");
         noteRepository.deleteById(id);
         return note;
    }
    public NoteDto updateNote(NoteDto n,long id){
        UserDetails userDetails = getUserDetail();
        NoteDto note = noteRepository.findByIdAndUsername(id,userDetails.getUsername());
        if(note == null) throw new ResourceNotFoundException("Note not found");
        note.setName(n.getName().trim());
        note.setDone(n.getDone());
        note.setDescription(n.getDescription().trim());
        noteRepository.updateNote(note);
        return note;
    }
    public NoteDto patchNote(Map<String,Object> changes, long id){
        UserDetailsImpl user = getUserDetail();
        NoteDto note = noteRepository.findByIdAndUsername(id, user.getUsername());
        if(note == null) throw new ResourceNotFoundException("Note not found");

        changes.forEach((key,value) -> {
            Field field = ReflectionUtils.findField(NoteDto.class,key);
            if(field != null){
                ReflectionUtils.makeAccessible(field);
                if(value.getClass().equals(String.class)){
                    value = ((String) value).trim();
                }
                ReflectionUtils.setField(field,note,value);
            }
            else throw new WrongFieldException("Wrong field : " + key);
        });
        noteRepository.updateNote(note);
        return note;
    }

    private UserDetailsImpl getUserDetail(){
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
