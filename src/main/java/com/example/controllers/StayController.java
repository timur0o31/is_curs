package com.example.controllers;

import com.example.dto.StayDto;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.services.StayService;

@RestController
@RequestMapping("/api/stays")
@RequiredArgsConstructor
public class StayController {

    private final StayService stayService;

    @GetMapping
    public List<StayDto> getAll() {
        return stayService.getAll();
    }

    @GetMapping(params = "doctorId")
    public List<StayDto> getByDoctorId(@RequestParam Long doctorId) {
        return stayService.getByDoctorId(doctorId);
    }
    @PostMapping("/{id}/discharge")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> discharge(
            @PathVariable Long id,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dischargeDate
    ) {
        return ResponseEntity.ok(stayService.discharge(id, dischargeDate));
    }

    @GetMapping(params = "roomId")
    public List<StayDto> getByRoomId(@RequestParam Long roomId) {
        return stayService.getByRoomId(roomId);
    }

    @GetMapping("/{id}")
    public StayDto getById(@PathVariable Long id) {
        return stayService.getById(id);
    }

    @PostMapping
    public StayDto create(@RequestBody StayDto dto) {
        return stayService.create(dto);
    }

    @PutMapping("/{id}")
    public StayDto update(@PathVariable Long id, @RequestBody StayDto dto) {
        return stayService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        stayService.delete(id);
    }
}
