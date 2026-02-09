package com.example.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    // User permissions
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    USER_DELETE("user:delete"),

    // Patient permissions
    PATIENT_READ("patient:read"),
    PATIENT_WRITE("patient:write"),
    PATIENT_DELETE("patient:delete"),

    // Doctor permissions
    DOCTOR_READ("doctor:read"),
    DOCTOR_WRITE("doctor:write"),
    DOCTOR_DELETE("doctor:delete"),

    // Medical card permissions
    MEDICAL_CARD_READ("medical_card:read"),
    MEDICAL_CARD_WRITE("medical_card:write"),

    // Room permissions
    ROOM_READ("room:read"),
    ROOM_WRITE("room:write"),

    // Stay request permissions
    STAY_REQUEST_READ("stay_request:read"),
    STAY_REQUEST_WRITE("stay_request:write"),
    STAY_REQUEST_APPROVE("stay_request:approve"),

    // Session permissions
    SESSION_READ("session:read"),
    SESSION_WRITE("session:write"),
    SESSION_DELETE("session:delete"),

    // Prescription permissions
    PRESCRIPTION_READ("prescription:read"),
    PRESCRIPTION_WRITE("prescription:write"),

    // Locker permissions
    LOCKER_READ("locker:read"),
    LOCKER_WRITE("locker:write"),

    // Diary entry permissions
    DIARY_ENTRY_READ("diary_entry:read"),
    DIARY_ENTRY_WRITE("diary_entry:write");

    private final String permission;
}

