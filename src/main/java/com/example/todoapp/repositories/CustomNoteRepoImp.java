package com.example.todoapp.repositories;

import com.example.todoapp.models.Note;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.query.Query;

import java.util.List;

public class CustomNoteRepoImp implements CustomNoteRepo {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    @SuppressWarnings("unchecked")
    public List<Note> findNoteById(Long id){
        return entityManager.createQuery(
                "Select n from Note n where n.user.id = :id", Note.class)
                .unwrap(Query.class)
                .setParameter("id",id).getResultList();
    }
}
