package com.congdinh.vivustore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("/error/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }
}
