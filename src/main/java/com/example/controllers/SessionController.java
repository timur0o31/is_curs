package com.example.controllers;

import com.example.dto.SessionDto;
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
import com.example.services.SessionService;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {
    private final SessionService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SessionDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping(params = "doctorId")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SessionDto>> getByDoctorId(@RequestParam Long doctorId) {
        return ResponseEntity.ok(service.getByDoctorId(doctorId));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('DOCTOR') and @doctorAccess.canWork(authentication)")
    public ResponseEntity<List<SessionDto>> getMySessions(Authentication auth) {
        return ResponseEntity.ok(service.getByDoctorEmail(auth.getName()));
    }
    @PostMapping("/my")
    @PreAuthorize("hasRole('DOCTOR') and @doctorAccess.canWork(authentication)")
    public ResponseEntity<SessionDto> createMy(Authentication auth, @RequestBody SessionDto dto) {
        return ResponseEntity.ok(service.createForDoctor(auth.getName(), dto));
    }
    @GetMapping(params = "procedureId")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SessionDto>> getByProcedureId(@RequestParam Long procedureId) {
        return ResponseEntity.ok(service.getByProcedureId(procedureId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SessionDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SessionDto> create(@RequestBody SessionDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SessionDto> update(@PathVariable Long id, @RequestBody SessionDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
