package com.example.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.services.AuthService;
import com.example.services.UserService;
import com.example.models.Role;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthService.AuthResponse> login(@RequestBody LoginRequest request) {
        AuthService.AuthResponse response = authService.authenticate(request.email, request.password);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (request.role != null && !request.role.equalsIgnoreCase("PATIENT")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Регистрация доступна только для PATIENT");
        }
        userService.createUser(
                request.email,
                request.password,
                request.name,
                Role.PATIENT
        );
        return ResponseEntity.ok("Пользователь успешно зарегистрирован");
    }

    public record LoginRequest(String email, String password) {}

    public record RegisterRequest(String email, String password, String name, String role) {}
}
