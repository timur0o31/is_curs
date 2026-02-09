package com.example.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<Map<String, String>> getDoctorDashboard(Authentication authentication) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Панель врача");
        response.put("user", authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patients")
    @PreAuthorize("hasAuthority('patient:read')")
    public ResponseEntity<String> getPatients() {
        return ResponseEntity.ok("Список пациентов врача");
    }

    @GetMapping("/medical-cards/{id}")
    @PreAuthorize("hasAuthority('medical_card:read')")
    public ResponseEntity<String> getMedicalCard(@PathVariable Long id) {
        return ResponseEntity.ok("Медицинская карта пациента #" + id);
    }

    @PostMapping("/medical-cards/{id}")
    @PreAuthorize("hasAuthority('medical_card:write')")
    public ResponseEntity<String> updateMedicalCard(
            @PathVariable Long id,
            @RequestBody Map<String, Object> data) {
        return ResponseEntity.ok("Медицинская карта #" + id + " обновлена");
    }

    @GetMapping("/sessions")
    @PreAuthorize("hasAuthority('session:read')")
    public ResponseEntity<String> getSessions() {
        return ResponseEntity.ok("Список сеансов врача");
    }

    @PostMapping("/sessions")
    @PreAuthorize("hasAuthority('session:write')")
    public ResponseEntity<String> createSession(@RequestBody Map<String, Object> data) {
        return ResponseEntity.ok("Сеанс создан");
    }

    @PostMapping("/prescriptions")
    @PreAuthorize("hasAuthority('prescription:write')")
    public ResponseEntity<String> createPrescription(@RequestBody Map<String, Object> data) {
        return ResponseEntity.ok("Рецепт создан");
    }

    @GetMapping("/prescriptions/{id}")
    @PreAuthorize("hasAuthority('prescription:read')")
    public ResponseEntity<String> getPrescription(@PathVariable Long id) {
        return ResponseEntity.ok("Рецепт #" + id);
    }
}

