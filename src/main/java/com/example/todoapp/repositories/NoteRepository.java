package com.example.todoapp.repositories;

import com.example.todoapp.dto.NoteDto;
import com.example.todoapp.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NoteRepository extends JpaRepository<Note,Long>{
    @Query("Delete from Note where id = :id")
    @Modifying(flushAutomatically = true,clearAutomatically = true)
    void deleteById(long id);
    @Query("""
            SELECT new com.example.todoapp.dto.NoteDto(n.id,n.name,n.description,n.done)\s
            from Note n join n.user u \s
            where u.userName = :userName\s
            """)
    List<NoteDto> findByUsername(@Param("userName") String userName);
    @Query("""
            SELECT new com.example.todoapp.dto.NoteDto(n.id,n.name,n.description,n.done)
            from Note n join n.user u
            where u.userName = :userName and n.id = :id
            """)
    NoteDto findByIdAndUsername(long id, String userName);
    @Query("""
            Update Note n \s
            set n.name = :#{#note.name},n.description = :#{#note.description},n.done = :#{#note.done}
            where n.id = :#{#note.id}
            """)
    @Modifying(flushAutomatically = true,clearAutomatically = true)
    void updateNote(@Param("note") NoteDto note);
}
