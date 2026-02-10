package com.example.services;

import com.example.dto.ProcedureDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.example.mapper.ProcedureMapper;
import com.example.models.Procedure;
import org.springframework.stereotype.Service;
import com.example.repositories.ProcedureRepository;

@Service
@RequiredArgsConstructor
public class ProcedureService {
    private final ProcedureRepository repository;
    private final ProcedureMapper mapper;

    public List<ProcedureDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public ProcedureDto getById(Long id) {
        Procedure entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Procedure not found: " + id));
        return mapper.toDto(entity);
    }

    public ProcedureDto create(ProcedureDto dto) {
        if (dto.getIsOptional() == null) {
            dto.setIsOptional(false);
        }
        Procedure entity = mapper.toEntity(dto);
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    public ProcedureDto update(Long id, ProcedureDto dto) {
        if (dto.getIsOptional() == null) {
            dto.setIsOptional(false);
        }
        Procedure entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<ProcedureDto> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name).stream().map(mapper::toDto).toList();
    }
}
