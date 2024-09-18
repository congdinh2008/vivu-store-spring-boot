package com.congdinh.vivustore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public String index() {
        return "home/index";
    }

    @GetMapping("about")
    public String about() {
        return "home/about";
    }
    
    @GetMapping("contact")
    public String contact() {
        return "home/contact";
    }
}
