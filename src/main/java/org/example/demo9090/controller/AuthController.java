package org.example.demo9090.controller;

import org.example.demo9090.service.JwtService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final JwtService jwtService;

    public AuthController(JwtService jwtService) { this.jwtService = jwtService; }

    @GetMapping("/auth/login")
    public String login(@RequestParam String user) {
        return jwtService.generateToken(user);
    }

    @GetMapping("/api/secure")
    public String secure() {
        return "You accessed a secure endpoint!";
    }
}
