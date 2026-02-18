package com.example.controllers;

import com.example.dto.RoomDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.example.services.RoomService;
import com.example.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoomDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/available")
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoomDto> getAvailable() {
        return service.getAvailableRooms();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RoomDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/by-patient/{patientId}/number")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR') and @doctorAccess.canWork(authentication)")
    public ResponseEntity<Integer> getRoomNumberByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(service.getRoomNumberByPatientId(patientId));
    }

    @GetMapping("/my/number")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Integer> getMyRoomNumber(@AuthenticationPrincipal UserDetailsImpl user) {
        return ResponseEntity.ok(service.getRoomNumberByUserId(user.getId()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public RoomDto create(@RequestBody RoomDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RoomDto update(@PathVariable Long id, @RequestBody RoomDto dto) {
        return service.update(id, dto);
    }

    @PutMapping("/{id}/occupancy")
    @PreAuthorize("hasRole('ADMIN')")
    public RoomDto updateOccupancy(@PathVariable Long id, @RequestParam Boolean isOccupied) {
        return service.updateOccupancy(id, isOccupied);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
