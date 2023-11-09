package com.example.todoapp.repositories;

import com.example.todoapp.dto.LoginDto;
import com.example.todoapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository


public interface UserRepository extends JpaRepository<User,Long> {

    User findUserByUserName(String userName);
    User findUserByEmail(String email);
}
