package com.congdinh.vivustore.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.congdinh.vivustore.entities.Role;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findByName(String name);
}
