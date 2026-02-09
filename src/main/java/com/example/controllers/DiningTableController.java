package com.example.controllers;

import com.example.dto.DiningTableDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.example.models.Diet;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.services.DiningTableService;

@RestController
@RequestMapping("/api/dining-tables")
@RequiredArgsConstructor
public class DiningTableController {
    private final DiningTableService service;

    @GetMapping
    public List<DiningTableDto> getAll() {
        return service.getAll();
    }

    @GetMapping(params = "diet")
    public List<DiningTableDto> getByDiet(@RequestParam Diet diet) {
        return service.getByDiet(diet);
    }

    @GetMapping("/{id}")
    public DiningTableDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public DiningTableDto create(@RequestBody DiningTableDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public DiningTableDto update(@PathVariable Long id, @RequestBody DiningTableDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
