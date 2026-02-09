package controller;

import dto.MedicalCardDto;
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
import service.MedicalCardService;

@RestController
@RequestMapping("/api/medical-cards")
@RequiredArgsConstructor
public class MedicalCardController {
    private final MedicalCardService service;

    @GetMapping
    public List<MedicalCardDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/by-patient/{patientId}")
    public MedicalCardDto getByPatientId(@PathVariable Long patientId) {
        return service.getByPatientId(patientId);
    }

    @GetMapping("/{id}")
    public MedicalCardDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public MedicalCardDto create(@RequestBody MedicalCardDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public MedicalCardDto update(@PathVariable Long id, @RequestBody MedicalCardDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
