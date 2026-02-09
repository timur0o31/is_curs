package com.example.services;

import lombok.RequiredArgsConstructor;
import com.example.models.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.example.repositories.UserRepository;
import com.example.security.JwtService;
import com.example.security.UserDetailsImpl;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse authenticate(String email, String password) {
        // Аутентифицируем пользователя
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        if (!user.isEnabled()) {
            throw new IllegalStateException("Учетная запись отключена");
        }

        if (user.isLocked()) {
            throw new IllegalStateException("Учетная запись заблокирована");
        }

        // Генерируем токены
        String token = jwtService.generateToken(email, user.getRole(), userDetails.getAuthorities());
        String refreshToken = jwtService.generateRefreshToken(email);

        return new AuthResponse(token, refreshToken, user.getRole().name(), user.getEmail(), user.getName());
    }

    public record AuthResponse(String token, String refreshToken, String role, String email, String name) {}
}

