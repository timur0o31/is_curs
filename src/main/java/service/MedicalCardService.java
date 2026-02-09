package service;

import dto.MedicalCardDto;
import lombok.RequiredArgsConstructor;
import mapper.MedicalCardMapper;
import model.MedicalCard;
import org.springframework.stereotype.Service;
import repository.MedicalCardRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalCardService {
    private final MedicalCardRepository repository;
    private final MedicalCardMapper mapper;

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
}
