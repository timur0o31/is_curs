package com.example.services;

import com.example.dto.PrescriptionDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.example.mapper.PrescriptionMapper;
import com.example.models.Prescription;
import org.springframework.stereotype.Service;
import com.example.repositories.PrescriptionRepository;

@Service
@RequiredArgsConstructor
public class PrescriptionService {
    private final PrescriptionRepository repository;
    private final PrescriptionMapper mapper;

    public List<PrescriptionDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public PrescriptionDto getById(Long id) {
        Prescription entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found: " + id));
        return mapper.toDto(entity);
    }

    public PrescriptionDto create(PrescriptionDto dto) {
        Prescription entity = mapper.toEntity(dto);
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    public PrescriptionDto update(Long id, PrescriptionDto dto) {
        Prescription entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<PrescriptionDto> getByMedicalCardId(Long medicalCardId) {
        return repository.findByMedicalCard_Id(medicalCardId).stream().map(mapper::toDto).toList();
    }

    public List<PrescriptionDto> getByMedicamentId(Long medicamentId) {
        return repository.findByMedicament_Id(medicamentId).stream().map(mapper::toDto).toList();
    }
}
