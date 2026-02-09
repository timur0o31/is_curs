package com.example.services;

import com.example.dto.LockerDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.example.mapper.LockerMapper;
import com.example.models.Locker;
import org.springframework.stereotype.Service;
import com.example.repositories.LockerRepository;

@Service
@RequiredArgsConstructor
public class LockerService {
    private final LockerRepository repository;
    private final LockerMapper mapper;

    public List<LockerDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public LockerDto getById(Long id) {
        Locker entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Locker not found: " + id));
        return mapper.toDto(entity);
    }

    public LockerDto create(LockerDto dto) {
        Locker entity = mapper.toEntity(dto);
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    public LockerDto update(Long id, LockerDto dto) {
        Locker entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public LockerDto getByPatientId(Long patientId) {
        Locker entity = repository.findByPatientId(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Locker not found for patient: " + patientId));
        return mapper.toDto(entity);
    }

    public List<LockerDto> getAvailableLockers() {
        return repository.findByPatientIdIsNull().stream().map(mapper::toDto).toList();
    }
}
