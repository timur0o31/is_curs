package com.example.services;

import com.example.dto.MedicalCardDto;
import com.example.mapper.MedicalCardMapper;
import com.example.models.Diet;
import com.example.models.MedicalCard;
import com.example.repositories.MedicalCardRepository;
import com.example.services.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalCardService {
    private final MedicalCardRepository repository;
    private final MedicalCardMapper mapper;
    private final SeatService seatService;

    public List<MedicalCardDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public MedicalCardDto getById(Long id) {
        MedicalCard entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MedicalCard not found: " + id));
        return mapper.toDto(entity);
    }

    public MedicalCardDto create(MedicalCardDto dto) {
        MedicalCard entity = mapper.toEntity(dto);
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    public MedicalCardDto createForPatient(Long patientId) {
        MedicalCard entity = new MedicalCard();
        entity.setPatientId(patientId);
        return mapper.toDto(repository.save(entity));
    }

    public MedicalCardDto update(Long id, MedicalCardDto dto) {
        MedicalCard entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public MedicalCardDto getByPatientId(Long patientId) {
        MedicalCard entity = repository.findByPatientId(patientId)
                .orElseThrow(() -> new IllegalArgumentException("MedicalCard not found for patient: " + patientId));
        return mapper.toDto(entity);
    }

    public MedicalCardDto updateDietAndAssignSeat(Long patientId, Diet newDiet) {
        MedicalCard entity = repository.findByPatientId(patientId)
                .orElseThrow(() -> new IllegalArgumentException("MedicalCard not found for patient: " + patientId));
        entity.setDiet(newDiet);
        MedicalCard saved = repository.save(entity);

        seatService.assignSeatForPatientByDiet(patientId, newDiet);

        return mapper.toDto(saved);
    }
}
