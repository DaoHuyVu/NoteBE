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

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Transactional(readOnly = true)
@Service

public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    private static final Logger logger = LoggerFactory.getLogger(NoteService.class);

    @Autowired
    private UserRepository userRepository;

    public NoteDto getNoteDtoById(long id) {
        UserDetails userDetails = getUserDetail();
            Note note = noteRepository.findByIdAndUsername(id, userDetails.getUsername());
            if(note == null) throw new ResourceNotFoundException("Note not found");
        return note.toNoteDto();
    }
    public List<NoteDto> getAllNotes(){
        UserDetails userDetails = getUserDetail();
        return noteRepository.findByUsername(userDetails.getUsername()).stream().map(Note::toNoteDto).toList();
    }
    @Transactional
    public NoteDto addNote(String name,String description,String createdAt,String done){

        UserDetailsImpl userDetails = getUserDetail();
        User user = userRepository.getReferenceById(userDetails.getId());
        Note note = noteRepository.save(
                new Note(
                        name.trim(),
                        description.trim(),
                        LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(Long.parseLong(createdAt)),
                                ZoneId.of("UTC+0")),
                        Boolean.parseBoolean(done),
                        user));
        return note.toNoteDto();
    }
    @Transactional
    public NoteDto deleteNote(long id){
         UserDetails userDetails = getUserDetail();
         Note note = noteRepository.findByIdAndUsername(id,userDetails.getUsername());
         if(note == null) throw new ResourceNotFoundException("Note not found");
         noteRepository.deleteById(id);
         return note.toNoteDto();
    }
    @Transactional
    public NoteDto updateNote(String name,String description,long id){
        UserDetails userDetails = getUserDetail();
        Note note = noteRepository.findByIdAndUsername(id,userDetails.getUsername());
        if(note == null) throw new ResourceNotFoundException("Note not found");
        note.setName(name.trim());
        note.setDescription(description.trim());
        return note.toNoteDto();
    }
    @Transactional
    public NoteDto patchNote(Map<String,String> changes, long id){
        UserDetailsImpl user = getUserDetail();
        Note note = noteRepository.findByIdAndUsername(id, user.getUsername());
        if(note == null) throw new ResourceNotFoundException("Note not found");

        changes.forEach((key,value) -> {
            Field field = ReflectionUtils.findField(Note.class,key);
            if(field != null){
                ReflectionUtils.makeAccessible(field);
                if(field.getType().getCanonicalName().equals(Boolean.class.getCanonicalName())){
                    ReflectionUtils.setField(field,note,Boolean.valueOf(value));
                }
                else ReflectionUtils.setField(field,note,value.trim());
            }
            else throw new WrongFieldException("Wrong field : " + key);
        });
        return note.toNoteDto();
    }

    private UserDetailsImpl getUserDetail(){
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
