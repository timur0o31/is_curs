package com.example.services;

import com.example.dto.RegistrationDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.example.mapper.RegistrationMapper;
import com.example.models.Registration;
import com.example.models.Registration.RegistrationId;
import org.springframework.stereotype.Service;
import com.example.repositories.RegistrationRepository;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationRepository repository;
    private final RegistrationMapper mapper;

    public List<RegistrationDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public RegistrationDto getById(Long stayId, Long sessionId) {
        RegistrationId id = new RegistrationId(stayId, sessionId);
        Registration entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registration not found: " + stayId + "/" + sessionId));
        return mapper.toDto(entity);
    }

    public RegistrationDto create(RegistrationDto dto) {
        Registration entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public RegistrationDto update(Long stayId, Long sessionId, RegistrationDto dto) {
        Registration entity = mapper.toEntity(dto);
        entity.setId(new RegistrationId(stayId, sessionId));
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long stayId, Long sessionId) {
        repository.deleteById(new RegistrationId(stayId, sessionId));
    }

    public List<RegistrationDto> getByStayId(Long stayId) {
        return repository.findByStay_Id(stayId).stream().map(mapper::toDto).toList();
    }

    public List<RegistrationDto> getBySessionId(Long sessionId) {
        return repository.findBySession_Id(sessionId).stream().map(mapper::toDto).toList();
    }
}
