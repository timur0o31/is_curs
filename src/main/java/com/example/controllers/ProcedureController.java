package controller;

import dto.ProcedureDto;
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
import service.ProcedureService;

@RestController
@RequestMapping("/api/procedures")
@RequiredArgsConstructor
public class ProcedureController {
    private final ProcedureService service;

    @GetMapping
    public List<ProcedureDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ProcedureDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public ProcedureDto create(@RequestBody ProcedureDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public ProcedureDto update(@PathVariable Long id, @RequestBody ProcedureDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/search")
    public List<ProcedureDto> searchByName(@RequestParam String name) {
        return service.searchByName(name);
    }
}
