package com.congdinh.vivustore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.congdinh.vivustore.dtos.auth.RegisterDTO;
import com.congdinh.vivustore.services.AuthService;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerPost(@ModelAttribute("user") RegisterDTO registerDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        if (authService.existsByUsername(registerDTO.getUsername())) {
            bindingResult.rejectValue("username", "", "Username is duplicate");
            return "auth/register";
        }

        var id = authService.save(registerDTO);

        if (id == null) {
            bindingResult.rejectValue("username", "", "An error occurred");
            return "auth/register";
        }
        
        return "redirect:/auth/login";
    }
}
