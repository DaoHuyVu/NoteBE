package com.example.todoapp.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Entity(name="Note")
@AllArgsConstructor
@NoArgsConstructor
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
}
