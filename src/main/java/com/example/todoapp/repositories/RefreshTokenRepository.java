package com.example.todoapp.repositories;

import com.example.todoapp.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByUserIdAndId(Long userId, Long id);
    @Modifying
    @Query("""
            Delete from RefreshToken rf where rf.user.id = :userId
            """)
    void deleteByUserId(Long userId);
}
