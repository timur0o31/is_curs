package com.example.controllers;

import com.example.dto.SessionDto;
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
import com.example.services.SessionService;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {
    private final SessionService service;

    @GetMapping
    public List<SessionDto> getAll() {
        return service.getAll();
    }

    @GetMapping(params = "doctorId")
    public List<SessionDto> getByDoctorId(@RequestParam Long doctorId) {
        return service.getByDoctorId(doctorId);
    }

    @GetMapping(params = "procedureId")
    public List<SessionDto> getByProcedureId(@RequestParam Long procedureId) {
        return service.getByProcedureId(procedureId);
    }

    @GetMapping("/{id}")
    public SessionDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public SessionDto create(@RequestBody SessionDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public SessionDto update(@PathVariable Long id, @RequestBody SessionDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
