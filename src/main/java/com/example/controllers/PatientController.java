package com.example.controllers;

import com.example.dto.MedicalCardDto;
import com.example.dto.PrescriptionDto;
import com.example.security.UserDetailsImpl;
import com.example.services.MedicalCardService;
import com.example.services.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final MedicalCardService medicalCardService;
    private final PrescriptionService prescriptionService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('PATIENT') or @doctorAccess.canWork(authentication)")
    public ResponseEntity<Map<String, String>> getPatientDashboard(Authentication authentication) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Панель пациента");
        response.put("user", authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/medical-card")
    @PreAuthorize("hasAuthority('medical_card:read')")
    public ResponseEntity<String> getMyMedicalCard(Authentication authentication) {
        return ResponseEntity.ok("Моя медицинская карта для " + authentication.getName());
    }

    @GetMapping("/stay-requests")
    @PreAuthorize("hasAuthority('stay_request:read')")
    public ResponseEntity<String> getMyStayRequests() {
        return ResponseEntity.ok("Мои заявки на пребывание");
    }

    @PostMapping("/stay-requests")
    @PreAuthorize("hasAuthority('stay_request:write')")
    public ResponseEntity<String> createStayRequest(@RequestBody Map<String, Object> data) {
        return ResponseEntity.ok("Заявка на пребывание создана");
    }

    @GetMapping("/sessions")
    @PreAuthorize("hasAuthority('session:read')")
    public ResponseEntity<String> getMySessions() {
        return ResponseEntity.ok("Мои сеансы");
    }

    @GetMapping("/prescriptions")
    @PreAuthorize("hasAuthority('prescription:read')")
    public ResponseEntity<List<PrescriptionDto>> getMyPrescriptions(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // Получаем ID пациента из текущего пользователя
        Long userId = userDetails.getId();

        // Получаем медицинскую карту пациента
        MedicalCardDto medicalCard = medicalCardService.getByPatientUserId(userId);

        // Получаем все рецепты по медицинской карте
        List<PrescriptionDto> prescriptions = prescriptionService.getByMedicalCardId(medicalCard.getId());

        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/room")
    @PreAuthorize("hasAuthority('room:read')")
    public ResponseEntity<String> getMyRoom() {
        return ResponseEntity.ok("Информация о моей комнате");
    }

    @GetMapping("/locker")
    @PreAuthorize("hasAuthority('locker:read')")
    public ResponseEntity<String> getMyLocker() {
        return ResponseEntity.ok("Информация о моем шкафчике");
    }
}
