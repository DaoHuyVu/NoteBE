package com.example.todoapp.models;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class RefreshToken {
    @Id
    @Tsid
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private Long createdAt;
    private Long expirationTime;
}
