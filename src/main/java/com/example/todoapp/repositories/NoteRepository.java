package com.example.todoapp.repositories;

import com.example.todoapp.dto.NoteDto;
import com.example.todoapp.models.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            SELECT new com.example.todoapp.models.Note(n.id,n.name,n.description,n.done,n.createdAt)\s
            from Note n join n.user u \s
            where u.userName = :userName\s
            """)
    List<Note> findByUsername(@Param("userName") String userName);
    @Query("""
            SELECT n
            from Note n join n.user u
            where u.userName = :userName and n.id = :id
            """)
    Note findByIdAndUsername(long id, String userName);
    @Query("""
            Select new com.example.todoapp.dto.NoteDto(
            n.id,n.name,n.description,n.done,n.createdAt
            )
            from Note n where n.user.id = :id
            """)
    Page<NoteDto> getNotes(Pageable pageable,Long id);
}
