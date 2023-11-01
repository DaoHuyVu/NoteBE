package com.example.todoapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;
@NoArgsConstructor
@Entity(name = "User")
@Data
@Table(name = "user")
public class User {
    public User(String email,String userName,String password) {
        this.email = email;
        this.userName = userName;
        this.password = password;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true,nullable = false)
    private String userName;
    @Column(nullable = false)
    private String password;
    @Column(unique = true,nullable = false)
    private String email;
}