package com.example.controllers.stay;

import com.example.dto.stay.StayDto;
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
import com.example.services.stay.StayService;

@RestController
@RequestMapping("/api/stays")
@RequiredArgsConstructor
public class StayController {

    private final StayService service;

    @GetMapping
    public List<StayDto> getAll() {
        return service.getAll();
    }

    @GetMapping(params = "doctorId")
    public List<StayDto> getByDoctorId(@RequestParam Long doctorId) {
        return service.getByDoctorId(doctorId);
    }

    @GetMapping(params = "roomId")
    public List<StayDto> getByRoomId(@RequestParam Long roomId) {
        return service.getByRoomId(roomId);
    }

    @GetMapping("/{id}")
    public StayDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public StayDto create(@RequestBody StayDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public StayDto update(@PathVariable Long id, @RequestBody StayDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
