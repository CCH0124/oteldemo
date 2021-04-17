package com.otel.example.oteldemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
public class IndexController {
    @GetMapping
    public String sayHello() {
        return "Hello and Welcome to the application. You can create a new Note by making a POST request to /api/notes endpoint.";
    }
}
