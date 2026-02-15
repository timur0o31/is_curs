package com.example.controllers;

import com.example.dto.DiaryEntryDto;
import com.example.services.DiaryEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diary-entries")
@RequiredArgsConstructor
public class DiaryEntryController {
    private final DiaryEntryService service;

    @GetMapping
    public List<DiaryEntryDto> getAll() {
        return service.getAll();
    }

    @GetMapping(params = "medicalCardId")
    public List<DiaryEntryDto> getByMedicalCardId(@RequestParam Long medicalCardId) {
        return service.getByMedicalCardId(medicalCardId);
    }

    @GetMapping("/{id}")
    public DiaryEntryDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public DiaryEntryDto create(@RequestBody DiaryEntryDto dto) {
        return service.create(dto);
    }

    @PostMapping("/medical-card/{medicalCardId}")
    public DiaryEntryDto createForMedicalCard(
            @PathVariable Long medicalCardId,
            @RequestBody String comment) {
        return service.createForMedicalCard(medicalCardId, comment);
    }

    @PutMapping("/{id}")
    public DiaryEntryDto update(@PathVariable Long id, @RequestBody DiaryEntryDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN') and @doctorAccess.canWork(authentication)")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
