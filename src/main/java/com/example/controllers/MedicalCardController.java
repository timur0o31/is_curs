package com.example.controllers;

import com.example.dto.MedicalCardDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.services.MedicalCardService;
import com.example.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/medical-cards")
@RequiredArgsConstructor
public class MedicalCardController {
    private final MedicalCardService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<MedicalCardDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('PATIENT')")
    public MedicalCardDto getMy(@AuthenticationPrincipal UserDetailsImpl user) {
        return service.getByPatientUserId(user.getId());
    }

    @GetMapping("/patients/{patientId}")
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN') and @doctorAccess.canWork(authentication)")
    public MedicalCardDto getByPatientId(
            Authentication auth,
            @AuthenticationPrincipal UserDetailsImpl user,
            @PathVariable Long patientId) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return service.getByPatientId(patientId);
        }
        return service.getByPatientIdForDoctorUser(patientId, user.getId());
    }

    @GetMapping("/by-patient/{patientId}")
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN') and @doctorAccess.canWork(authentication)")
    public MedicalCardDto getByPatientIdLegacy(
            Authentication auth,
            @AuthenticationPrincipal UserDetailsImpl user,
            @PathVariable Long patientId) {
        return getByPatientId(auth, user, patientId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MedicalCardDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public MedicalCardDto create(@RequestBody MedicalCardDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MedicalCardDto update(@PathVariable Long id, @RequestBody MedicalCardDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
