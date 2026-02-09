package service;

import dto.SeatDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mapper.SeatMapper;
import model.Seat;
import org.springframework.stereotype.Service;
import repository.SeatRepository;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository repository;
    private final SeatMapper mapper;

    public List<SeatDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public SeatDto getById(Long id) {
        Seat entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found: " + id));
        return mapper.toDto(entity);
    }

    public SeatDto create(SeatDto dto) {
        Seat entity = mapper.toEntity(dto);
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    public SeatDto update(Long id, SeatDto dto) {
        Seat entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<SeatDto> getByPatientId(Long patientId) {
        return repository.findByPatientId(patientId).stream().map(mapper::toDto).toList();
    }

    public List<SeatDto> getByDiningTableId(Long diningTableId) {
        return repository.findByDiningTable_Id(diningTableId).stream().map(mapper::toDto).toList();
    }
}
