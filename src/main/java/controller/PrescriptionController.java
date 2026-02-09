package controller;

import dto.PrescriptionDto;
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
import service.PrescriptionService;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {
    private final PrescriptionService service;

    @GetMapping
    public List<PrescriptionDto> getAll() {
        return service.getAll();
    }

    @GetMapping(params = "medicalCardId")
    public List<PrescriptionDto> getByMedicalCardId(@RequestParam Long medicalCardId) {
        return service.getByMedicalCardId(medicalCardId);
    }

    @GetMapping(params = "medicamentId")
    public List<PrescriptionDto> getByMedicamentId(@RequestParam Long medicamentId) {
        return service.getByMedicamentId(medicamentId);
    }

    @GetMapping("/{id}")
    public PrescriptionDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public PrescriptionDto create(@RequestBody PrescriptionDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public PrescriptionDto update(@PathVariable Long id, @RequestBody PrescriptionDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
