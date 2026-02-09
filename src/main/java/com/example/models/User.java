package com.example.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "\"User\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    /**
     * Основная роль пользователя в системе.
     * Если понадобится поддержать несколько ролей на пользователя,
     * можно будет заменить на коллекцию ролей.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String name;

    @Column(name = "is_enabled")
    private boolean enabled = true;

    @Column(name = "is_locked")
    private boolean locked = false;

    /**
     * Удобный хелпер: активен ли пользователь с точки зрения доступа.
     */
    public boolean isActive() {
        return enabled && !locked;
    }

    /**
     * Проверка, обладает ли пользователь конкретной ролью.
     */
    public boolean hasRole(Role role) {
        return this.role == role;
    }

    /**
     * Проверка, обладает ли пользователь конкретным правом (Permission)
     * через свою роль.
     */
    public boolean hasPermission(Permission permission) {
        return role != null && role.hasPermission(permission);
    }
}


