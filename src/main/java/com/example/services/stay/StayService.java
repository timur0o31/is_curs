package com.example.services.stay;

import com.example.dto.StayDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.example.mapper.StayMapper;
import com.example.models.Stay;
import org.springframework.stereotype.Service;
import com.example.repositories.StayRepository;

@Service
@RequiredArgsConstructor
public class StayService {
    private final StayRepository repository;
    private final StayMapper mapper;

    public List<StayDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public StayDto getById(Long id) {
        Stay entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Stay not found: " + id));
        return mapper.toDto(entity);
    }

    public StayDto create(StayDto dto) {
        Stay entity = mapper.toEntity(dto);
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    public StayDto update(Long id, StayDto dto) {
        Stay entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<StayDto> getByDoctorId(Long doctorId) {
        return repository.findByDoctorId(doctorId).stream().map(mapper::toDto).toList();
    }

    public List<StayDto> getByRoomId(Long roomId) {
        return repository.findByRoom_Id(roomId).stream().map(mapper::toDto).toList();
    }
}
