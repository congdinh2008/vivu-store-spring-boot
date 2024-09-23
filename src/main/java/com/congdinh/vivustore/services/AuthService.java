package com.congdinh.vivustore.services;

import com.congdinh.vivustore.dtos.auth.RegisterDTO;

public interface AuthService {
    boolean register(RegisterDTO registerDTO);

    boolean existsByUsername(String username);
}
