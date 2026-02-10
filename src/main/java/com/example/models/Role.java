package com.example.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.models.Permission.*;

@Getter
@RequiredArgsConstructor
public enum Role {
    /**
     * Полный доступ ко всей системе санатория.
     * Администратор может управлять пользователями и всеми доменными сущностями.
     */
    ADMIN(Set.of(
            USER_READ, USER_WRITE, USER_DELETE,
            PATIENT_READ, PATIENT_WRITE, PATIENT_DELETE,
            DOCTOR_READ, DOCTOR_WRITE, DOCTOR_DELETE,
            MEDICAL_CARD_READ, MEDICAL_CARD_WRITE,
            ROOM_READ, ROOM_WRITE,
            STAY_REQUEST_READ, STAY_REQUEST_WRITE, STAY_REQUEST_APPROVE,
            SESSION_READ, SESSION_WRITE, SESSION_DELETE,
            PRESCRIPTION_READ, PRESCRIPTION_WRITE,
            LOCKER_READ, LOCKER_WRITE,
            DIARY_ENTRY_READ, DIARY_ENTRY_WRITE
    )),

    /**
     * Врач работает с пациентами, их картами, назначениями и сессиями.
     */
    DOCTOR(Set.of(
            PATIENT_READ,
            MEDICAL_CARD_READ, MEDICAL_CARD_WRITE,
            STAY_REQUEST_READ,
            SESSION_READ, SESSION_WRITE,
            PRESCRIPTION_READ, PRESCRIPTION_WRITE,
            ROOM_READ,
            DIARY_ENTRY_READ, DIARY_ENTRY_WRITE
    )),

    /**
     * Пациент видит свои данные, заявки, сессии и назначения.
     */
    PATIENT(Set.of(
            MEDICAL_CARD_READ,
            STAY_REQUEST_READ, STAY_REQUEST_WRITE,
            SESSION_READ,
            PRESCRIPTION_READ,
            ROOM_READ,
            LOCKER_READ
    ));

    /**
     * Набор доменных прав (Permission), которыми обладает роль.
     */
    private final Set<Permission> permissions;

    /**
     * Проверка, обладает ли роль конкретным правом.
     */
    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    /**
     * Преобразование доменных прав и самой роли в набор строковых авторизаций
     * для интеграции, например, со Spring Security.
     * <p>
     * Пример: "ROLE_ADMIN", "user:read", "patient:write" и т.д.
     */
    public Set<String> getAuthorities() {
        Set<String> authorities = permissions.stream()
                .map(Permission::getPermission)
                .collect(Collectors.toCollection(() -> new HashSet<>(permissions.size() + 1)));

        authorities.add("ROLE_" + this.name());
        return Collections.unmodifiableSet(authorities);
    }
}
