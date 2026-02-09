package com.example.config;

import com.example.models.Role;
import com.example.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.bootstrap-admin.enabled", havingValue = "true")
public class AdminBootstrap implements ApplicationRunner {

    private final UserService userService;

    @Value("${app.bootstrap-admin.email:admin@example.com}")
    private String email;

    @Value("${app.bootstrap-admin.password:Admin123!}")
    private String password;

    @Value("${app.bootstrap-admin.name:Admin}")
    private String name;

    @Override
    public void run(ApplicationArguments args) {
        if (userService.findByEmail(email).isEmpty()) {
            userService.createUser(email, password, name, Role.ADMIN);
        }
    }
}
