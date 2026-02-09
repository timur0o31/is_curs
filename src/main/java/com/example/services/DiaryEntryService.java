package com.example.services;

import com.example.dto.DiaryEntryDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.example.mapper.DiaryEntryMapper;
import com.example.models.DiaryEntry;
import org.springframework.stereotype.Service;
import com.example.repositories.DiaryEntryRepository;

@Service
@RequiredArgsConstructor
public class DiaryEntryService {
    private final DiaryEntryRepository repository;
    private final DiaryEntryMapper mapper;

    public List<DiaryEntryDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public DiaryEntryDto getById(Long id) {
        DiaryEntry entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("DiaryEntry not found: " + id));
        return mapper.toDto(entity);
    }

    public DiaryEntryDto create(DiaryEntryDto dto) {
        DiaryEntry entity = mapper.toEntity(dto);
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    public DiaryEntryDto createForMedicalCard(Long medicalCardId, String comment) {
        DiaryEntryDto dto = new DiaryEntryDto(null, medicalCardId, comment);
        return create(dto);
    }

    public DiaryEntryDto update(Long id, DiaryEntryDto dto) {
        DiaryEntry entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<DiaryEntryDto> getByMedicalCardId(Long medicalCardId) {
        return repository.findByMedicalCard_Id(medicalCardId).stream().map(mapper::toDto).toList();
    }
}
