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
    private final RegistrationService service;

    @GetMapping
    public List<RegistrationDto> getAll() {
        return service.getAll();
    }

    @GetMapping(params = "stayId")
    public List<RegistrationDto> getByStayId(@RequestParam Long stayId) {
        return service.getByStayId(stayId);
    }

    @GetMapping(params = "sessionId")
    public List<RegistrationDto> getBySessionId(@RequestParam Long sessionId) {
        return service.getBySessionId(sessionId);
    }

    @GetMapping("/{stayId}/{sessionId}")
    public RegistrationDto getById(@PathVariable Long stayId, @PathVariable Long sessionId) {
        return service.getById(stayId, sessionId);
    }

    @PostMapping
    public RegistrationDto create(@RequestBody RegistrationDto dto) {
        return service.create(dto);
    }

    @PostMapping("/mandatory")
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
    public ResponseEntity<RegistrationDto> createMandatory(Authentication auth, @RequestBody RegistrationDto dto) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return ResponseEntity.ok(service.createMandatoryByAdmin(dto));
        }
        return ResponseEntity.ok(service.createMandatoryForDoctor(auth.getName(), dto));
    }

    @PutMapping("/{stayId}/{sessionId}")
    public RegistrationDto update(
            @PathVariable Long stayId,
            @PathVariable Long sessionId,
            @RequestBody RegistrationDto dto) {
        return service.update(stayId, sessionId, dto);
    }

    @DeleteMapping("/{stayId}/{sessionId}")
    public void delete(@PathVariable Long stayId, @PathVariable Long sessionId) {
        service.delete(stayId, sessionId);
    }
}
