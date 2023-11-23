package com.example.todoapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity(name = "RefreshToken")
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false,unique = true)
    private String token;
    @Column(nullable = false)
    private Instant expirationDate;
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    public RefreshToken(String token, Instant expirationDate, User user) {
        this.token = token;
        this.expirationDate = expirationDate;
        this.user = user;
    }
}
