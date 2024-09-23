package com.congdinh.vivustore.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

import com.congdinh.vivustore.dtos.auth.LoginDTO;
import com.congdinh.vivustore.dtos.auth.LoginResponseDTO;
import com.congdinh.vivustore.dtos.auth.RegisterDTO;
import com.congdinh.vivustore.services.AuthService;
import com.congdinh.vivustore.services.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping("/api/auth")
@Tag(name = "Auth")
public class AuthController {
    private final AuthService authService;

    private final TokenService tokenService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(AuthService authService, TokenService tokenService,
            AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.authService = authService;
        this.tokenService = tokenService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/login")
    @Operation(summary = "Login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword());

        // Call the authenticate method of the AuthenticationManagerBuilder instance
        // to authenticate the user
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenService.generateAccessToken(authentication);

        LoginResponseDTO loginResponse = new LoginResponseDTO();
        loginResponse.setAccessToken(accessToken);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/register")
    @Operation(summary = "Register")
    public ResponseEntity<Boolean> register(@Valid @RequestBody RegisterDTO registerDTO) {
        if (authService.existsByUsername(registerDTO.getUsername())) {
            return ResponseEntity.badRequest().body(false);
        }

        var result = authService.register(registerDTO);

        return ResponseEntity.ok(result);
    }
}
