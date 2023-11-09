package com.example.todoapp.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@NoArgsConstructor
@Entity(name="Note")
@Table(name="note")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private Boolean done;
    @ManyToOne(fetch =  FetchType.LAZY)
    private User user;

    public Note(String name, String description, Boolean done, User user) {
        this.name = name;
        this.description = description;
        this.done = done;
        this.user = user;
    }
}
