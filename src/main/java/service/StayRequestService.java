package service;

import dto.StayRequestDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mapper.StayRequestMapper;
import model.RequestStatus;
import model.StayRequest;
import org.springframework.stereotype.Service;
import repository.StayRequestRepository;

@Service
@RequiredArgsConstructor
public class StayRequestService {
    private final StayRequestRepository repository;
    private final StayRequestMapper mapper;

    public List<StayRequestDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public StayRequestDto getById(Long id) {
        StayRequest entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("StayRequest not found: " + id));
        return mapper.toDto(entity);
    }

    public StayRequestDto create(StayRequestDto dto) {
        StayRequest entity = mapper.toEntity(dto);
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    public StayRequestDto update(Long id, StayRequestDto dto) {
        StayRequest entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<StayRequestDto> getByPatientId(Long patientId) {
        return repository.findByPatientId(patientId).stream().map(mapper::toDto).toList();
    }

    public List<StayRequestDto> getByStatus(RequestStatus status) {
        return repository.findByStatus(status).stream().map(mapper::toDto).toList();
    }
}
