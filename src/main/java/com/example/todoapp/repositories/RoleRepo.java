package com.example.todoapp.repositories;

import com.example.todoapp.models.ERole;
import com.example.todoapp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByRole(ERole role);
}
