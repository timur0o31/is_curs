package com.example.services;

import com.example.dto.MedicamentDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.example.mapper.MedicamentMapper;
import com.example.models.Medicament;
import org.springframework.stereotype.Service;
import com.example.repositories.MedicamentRepository;

@Service
@RequiredArgsConstructor
public class MedicamentService {
    private final MedicamentRepository repository;
    private final MedicamentMapper mapper;

    public List<MedicamentDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public MedicamentDto getById(Long id) {
        Medicament entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medicament not found: " + id));
        return mapper.toDto(entity);
    }

    public MedicamentDto create(MedicamentDto dto) {
        Medicament entity = mapper.toEntity(dto);
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    public MedicamentDto update(Long id, MedicamentDto dto) {
        Medicament entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<MedicamentDto> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name).stream().map(mapper::toDto).toList();
    }
}
