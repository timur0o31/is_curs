package controller;

import dto.SeatDto;
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
import service.SeatService;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService service;

    @GetMapping
    public List<SeatDto> getAll() {
        return service.getAll();
    }

    @GetMapping(params = "patientId")
    public List<SeatDto> getByPatientId(@RequestParam Long patientId) {
        return service.getByPatientId(patientId);
    }

    @GetMapping(params = "diningTableId")
    public List<SeatDto> getByDiningTableId(@RequestParam Long diningTableId) {
        return service.getByDiningTableId(diningTableId);
    }

    @GetMapping("/{id}")
    public SeatDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public SeatDto create(@RequestBody SeatDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public SeatDto update(@PathVariable Long id, @RequestBody SeatDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
