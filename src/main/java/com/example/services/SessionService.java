package com.example.services;

import com.example.dto.SessionDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.example.mapper.SessionMapper;
import com.example.models.Session;
import org.springframework.stereotype.Service;
import com.example.repositories.SessionRepository;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository repository;
    private final SessionMapper mapper;

    public List<SessionDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public SessionDto getById(Long id) {
        Session entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + id));
        return mapper.toDto(entity);
    }

    public SessionDto create(SessionDto dto) {
        Session entity = mapper.toEntity(dto);
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    public SessionDto update(Long id, SessionDto dto) {
        Session entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<SessionDto> getByDoctorId(Long doctorId) {
        return repository.findByDoctorId(doctorId).stream().map(mapper::toDto).toList();
    }

    public List<SessionDto> getByProcedureId(Long procedureId) {
        return repository.findByProcedure_Id(procedureId).stream().map(mapper::toDto).toList();
    }
}
