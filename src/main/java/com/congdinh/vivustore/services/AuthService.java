package com.congdinh.vivustore.services;

import java.util.UUID;

import com.congdinh.vivustore.dtos.auth.RegisterDTO;

public interface AuthService {
    UUID save(RegisterDTO registerDTO);
    boolean existsByUsername(String username);
}