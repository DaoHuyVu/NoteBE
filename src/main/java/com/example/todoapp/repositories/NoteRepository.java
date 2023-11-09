package com.example.todoapp.repositories;

import com.example.todoapp.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface NoteRepository extends JpaRepository<Note,Long>,CustomNoteRepo {

}
