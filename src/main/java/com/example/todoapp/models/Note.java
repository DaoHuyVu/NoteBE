package com.example.todoapp.models;

import com.example.todoapp.dto.NoteDto;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Entity(name="Note")
@Table(name="note")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Boolean done;
    private LocalDateTime createdAt;
    @ManyToOne(fetch =  FetchType.LAZY)
    private User user;

    public Note(String name, String description, LocalDateTime createdAt, boolean done, User user) {
        this.name = name;
        this.description = description;
        this.done = done;
        this.user = user;
        this.createdAt = createdAt;
    }
    public NoteDto toNoteDto(){
        return new NoteDto(id,name,description,done,createdAt);
    }
}
