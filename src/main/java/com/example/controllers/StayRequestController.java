package com.example.controllers;

import com.example.dto.StayRequestDto;
import java.util.List;

import com.example.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import com.example.models.RequestStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.services.StayRequestService;

@RestController
@RequestMapping("/api/stay-requests")
@RequiredArgsConstructor
public class StayRequestController {
    private final StayRequestService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StayRequestDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping(params = "patientId")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StayRequestDto>> getByPatientId(@RequestParam Long patientId) {
        return ResponseEntity.ok(service.getByPatientId(patientId));
    }

    @GetMapping(params = "status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StayRequestDto>> getByStatus(@RequestParam RequestStatus status) {
        return ResponseEntity.ok(service.getByStatus(status));
    }
    @GetMapping("/my")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<StayRequestDto>> getMy(@AuthenticationPrincipal UserDetailsImpl user) {
        return ResponseEntity.ok(service.getByPatientId(user.getId()));
    }
    @PostMapping("/check-in")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<StayRequestDto> createCheckIn(Authentication auth,@RequestBody StayRequestDto dto) {
        return ResponseEntity.ok(
                service.createCheckInRequest(auth.getName(), dto.getAdmissionDate(), dto.getDischargeDate())
        );
    }
    @PostMapping("/expansion")
    @PreAuthorize("hasAuthority('stay_request:write')")
    public ResponseEntity<StayRequestDto> createExpansion(Authentication auth,@RequestBody StayRequestDto dto) {
        return ResponseEntity.ok(
                service.createExpansionRequest(auth.getName(), dto.getAdmissionDate(), dto.getDischargeDate())
        );
    }
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('stay_request:approve')")
    public ResponseEntity<Long> approve(@PathVariable Long id, @RequestParam Long roomId, @RequestParam(required = false) Long doctorId) {
        return ResponseEntity.ok(service.approveRequest(id, roomId, doctorId));
    }

    @PostMapping("/{id}/approve-expansion")
    @PreAuthorize("hasAuthority('stay_request:approve')")
    public ResponseEntity<StayRequestDto> approveExpansion(@PathVariable Long id) {
        return ResponseEntity.ok(service.approveExpansion(id));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('stay_request:approve')")
    public ResponseEntity<Void> reject(@PathVariable Long id) {
        service.rejectRequest(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('stay_request:write')")
    public ResponseEntity<StayRequestDto> update(@PathVariable Long id, @RequestBody StayRequestDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('stay_request:write')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Stay request deleated");
    }
}
