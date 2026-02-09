package controller;

import dto.DiaryEntryDto;
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
import service.DiaryEntryService;

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
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
