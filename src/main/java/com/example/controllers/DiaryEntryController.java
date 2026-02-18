package com.example.controllers;

import com.example.dto.DiaryEntryDto;
import com.example.dto.MedicalCardDto;
import com.example.security.UserDetailsImpl;
import com.example.services.DiaryEntryService;
import com.example.services.MedicalCardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diary-entries")
@RequiredArgsConstructor
public class DiaryEntryController {
    private final DiaryEntryService service;
    private final MedicalCardService medicalCardService;

    @GetMapping("/medical-cards/{medicalCardId}/diary-entries")
    @PreAuthorize("hasAuthority('diary_entry:read')")
    public List<DiaryEntryDto> getByMedicalCardId(
            @AuthenticationPrincipal UserDetailsImpl user,
            Authentication auth,
            @PathVariable Long medicalCardId) {
        assertPatientOwnsMedicalCard(medicalCardId, auth, user);
        return service.getByMedicalCardId(medicalCardId);
    }

    @PostMapping("/medical-cards/{medicalCardId}/diary-entries")
    @PreAuthorize("hasAuthority('diary_entry:write')")
    public DiaryEntryDto createForMedicalCard(
            @AuthenticationPrincipal UserDetailsImpl user,
            Authentication auth,
            @PathVariable Long medicalCardId,
            @RequestBody DiaryEntryCreateRequest request) {
        assertPatientOwnsMedicalCard(medicalCardId, auth, user);
        return service.createForMedicalCard(medicalCardId, request.comment());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN') and @doctorAccess.canWork(authentication)")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    public record DiaryEntryCreateRequest(String comment) {}

    private void assertPatientOwnsMedicalCard(Long medicalCardId, Authentication auth, UserDetailsImpl user) {
        if (auth == null || user == null) {
            return;
        }
        boolean isPatient = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"));
        if (!isPatient) {
            return;
        }
        MedicalCardDto own = medicalCardService.getByPatientUserId(user.getId());
        if (!medicalCardId.equals(own.getId())) {
            throw new AccessDeniedException("Нет доступа к чужой медкарте");
        }
    }
}
