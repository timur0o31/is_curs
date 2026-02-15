package com.example.controllers;

import com.example.dto.RegistrationDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.services.RegistrationService;

@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService RegService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR') and @doctorAccess.canWork(authentication)")
    public List<RegistrationDto> getAll() {
        return RegService.getAll();
    }

    @GetMapping(params = "stayId")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR') and @doctorAccess.canWork(authentication)")
    public List<RegistrationDto> getByStayId(@RequestParam Long stayId) {
        return RegService.getByStayId(stayId);
    }

    @GetMapping(params = "sessionId")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR') and @doctorAccess.canWork(authentication)")
    public List<RegistrationDto> getBySessionId(@RequestParam Long sessionId) {
        return RegService.getBySessionId(sessionId);
    }

    @GetMapping("/{stayId}/{sessionId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR') and @doctorAccess.canWork(authentication)")
    public RegistrationDto getById(@PathVariable Long stayId, @PathVariable Long sessionId) {
        return RegService.getById(stayId, sessionId);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<RegistrationDto>> getMyRegistrations(
            Authentication auth,
            @RequestParam(required = false) Boolean required) {
        return ResponseEntity.ok(RegService.getMyRegistrations(auth.getName(), required));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR') and @doctorAccess.canWork(authentication)")
    public RegistrationDto create(@RequestBody RegistrationDto dto) {
        return RegService.create(dto);
    }

    @PostMapping("/my")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<RegistrationDto> createMy(Authentication auth, @RequestBody RegistrationCreateRequest request) {
        return ResponseEntity.ok(RegService.createOptionalForPatient(auth.getName(), request.sessionId()));
    }

    @DeleteMapping("/my/{sessionId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<String> deleteMy(Authentication auth, @PathVariable Long sessionId) {
        RegService.deleteOptionalForPatient(auth.getName(), sessionId);
        return ResponseEntity.ok("Запись на необязательную процедуру отменена");
    }

    @PostMapping("/mandatory")
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN') and @doctorAccess.canWork(authentication)")
    public ResponseEntity<RegistrationDto> createMandatory(Authentication auth, @RequestBody RegistrationDto dto) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return ResponseEntity.ok(RegService.createMandatoryByAdmin(dto));
        }
        return ResponseEntity.ok(RegService.createMandatoryForDoctor(auth.getName(), dto));
    }

    @PutMapping("/{stayId}/{sessionId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR') and @doctorAccess.canWork(authentication)")
    public RegistrationDto update(
            @PathVariable Long stayId,
            @PathVariable Long sessionId,
            @RequestBody RegistrationDto dto) {
        return RegService.update(stayId, sessionId, dto);
    }

    @DeleteMapping("/{stayId}/{sessionId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR') and @doctorAccess.canWork(authentication)")
    public void delete(@PathVariable Long stayId, @PathVariable Long sessionId) {
        RegService.delete(stayId, sessionId);
    }

    public record RegistrationCreateRequest(Long sessionId) {}
}
