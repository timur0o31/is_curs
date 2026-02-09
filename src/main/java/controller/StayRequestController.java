package controller;

import dto.StayRequestDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import model.RequestStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.StayRequestService;

@RestController
@RequestMapping("/api/stay-requests")
@RequiredArgsConstructor
public class StayRequestController {
    private final StayRequestService service;

    @GetMapping
    public List<StayRequestDto> getAll() {
        return service.getAll();
    }

    @GetMapping(params = "patientId")
    public List<StayRequestDto> getByPatientId(@RequestParam Long patientId) {
        return service.getByPatientId(patientId);
    }

    @GetMapping(params = "status")
    public List<StayRequestDto> getByStatus(@RequestParam RequestStatus status) {
        return service.getByStatus(status);
    }

    @GetMapping("/{id}")
    public StayRequestDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public StayRequestDto create(@RequestBody StayRequestDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public StayRequestDto update(@PathVariable Long id, @RequestBody StayRequestDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
