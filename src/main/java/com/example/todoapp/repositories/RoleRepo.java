package com.example.todoapp.repositories;

import com.example.todoapp.models.ERole;
import com.example.todoapp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByRole(ERole role);
}
