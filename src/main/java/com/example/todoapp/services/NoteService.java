package com.example.todoapp.services;

import com.example.todoapp.dto.NoteDto;
import com.example.todoapp.exception.ResourceNotFoundException;
import com.example.todoapp.exception.WrongFieldException;
import com.example.todoapp.models.Note;
import com.example.todoapp.models.User;
import com.example.todoapp.repositories.NoteRepository;
import com.example.todoapp.repositories.UserRepository;
import com.example.todoapp.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.Instant;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<String,Object> getAllNotes(Integer page,Integer size,String[] sort){
        List<Sort.Order> orders = new ArrayList<>();
        if(sort[0].contains(",")){
            for(String sortOrder : sort){
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(Sort.Direction.valueOf(_sort[1]),_sort[0]));
            }
        }
        else {
            orders.add(new Sort.Order(Sort.Direction.valueOf(sort[1]),sort[0]));
        }
        Pageable request = PageRequest.of(page,size,Sort.by(orders));
        Page<NoteDto> dtoPage = noteRepository.getNotes(request,getUserDetail().getId());
        Map<String,Object> map =  new HashMap<>();
        map.put("notes",dtoPage.getContent());
        map.put("totalPages",dtoPage.getTotalPages());
        // page index is zero-based
        map.put("currentPage",dtoPage.getNumber()+1);
        map.put("totalItems",dtoPage.getTotalElements());
        return map;
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
