package com.congdinh.vivustore.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.congdinh.vivustore.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);

    boolean existsByUsername(String username);
}
