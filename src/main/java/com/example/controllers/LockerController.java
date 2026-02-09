package com.example.controllers;

import com.example.dto.LockerDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.services.LockerService;

@RestController
@RequestMapping("/api/lockers")
@RequiredArgsConstructor
public class LockerController {
    private final LockerService service;

    @GetMapping
    public List<LockerDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/available")
    public List<LockerDto> getAvailable() {
        return service.getAvailableLockers();
    }

    @GetMapping("/by-patient/{patientId}")
    public LockerDto getByPatientId(@PathVariable Long patientId) {
        return service.getByPatientId(patientId);
    }

    @GetMapping("/{id}")
    public LockerDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public LockerDto create(@RequestBody LockerDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public LockerDto update(@PathVariable Long id, @RequestBody LockerDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
