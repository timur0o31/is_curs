package com.example.controllers;

import com.example.dto.MedicamentDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.services.MedicamentService;

@RestController
@RequestMapping("/api/medicaments")
@RequiredArgsConstructor
public class MedicamentController {
    private final MedicamentService service;

    @GetMapping
    public List<MedicamentDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/search")
    public List<MedicamentDto> searchByName(@RequestParam String name) {
        return service.searchByName(name);
    }

    @GetMapping("/{id}")
    public MedicamentDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public MedicamentDto create(@RequestBody MedicamentDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public MedicamentDto update(@PathVariable Long id, @RequestBody MedicamentDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
