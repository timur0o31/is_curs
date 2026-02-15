package com.example.security;

import com.example.models.Doctor;
import com.example.repositories.DoctorRepository;
import com.example.services.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component("doctorAccess")
@RequiredArgsConstructor
public class DoctorAccess {

    private final DoctorRepository doctorRepository;
    private final UserService userService;

    public boolean canWork(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        if (hasAuthority(authentication, "ROLE_ADMIN")) {
            return true;
        }

        if (!hasAuthority(authentication, "ROLE_DOCTOR")) {
            return false;
        }

        return userService.findByEmail(authentication.getName())
                .flatMap(user -> doctorRepository.findByUser_Id(user.getId()))
                .map(Doctor::isWorking)
                .orElse(false);
    }

    private boolean hasAuthority(Authentication authentication, String authority) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority::equals);
    }
}
