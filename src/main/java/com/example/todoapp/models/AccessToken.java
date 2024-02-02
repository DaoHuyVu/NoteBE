package com.example.todoapp.models;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {
    @Id
    @Tsid
    private Long id;
    private Long expirationTime;
    private Long createdAt;
    @OneToOne(fetch = FetchType.LAZY)
    private RefreshToken refreshToken;
}
