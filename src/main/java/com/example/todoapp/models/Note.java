package com.example.todoapp.models;

import jakarta.persistence.*;
import lombok.*;


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
    private boolean done;
    @ManyToOne(fetch =  FetchType.LAZY)
    private User user;

    public Note(String name, String description, boolean done, User user) {
        this.name = name;
        this.description = description;
        this.done = done;
        this.user = user;
    }
}
