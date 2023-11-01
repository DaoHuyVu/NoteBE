package com.example.todoapp.repositories;

import com.example.todoapp.models.Note;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class CustomUserRepoImp implements CustomUserRepo{
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<Note> findNoteById(Long id){
        return entityManager.createQuery(
                "Select n from Note n where n.user.id = :id", Note.class)
                 .setParameter("id",id).getResultList();
    }
}
