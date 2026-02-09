package service;

import dto.RoomDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mapper.RoomMapper;
import model.Room;
import org.springframework.stereotype.Service;
import repository.RoomRepository;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository repository;
    private final RoomMapper mapper;

    public List<RoomDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public RoomDto getById(Long id) {
        Room entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + id));
        return mapper.toDto(entity);
    }

    public RoomDto create(RoomDto dto) {
        Room entity = mapper.toEntity(dto);
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    public RoomDto update(Long id, RoomDto dto) {
        Room entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDto(repository.save(entity));
    }

    public RoomDto updateOccupancy(Long id, Boolean isOccupied) {
        Room entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + id));
        entity.setIsOccupied(isOccupied);
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<RoomDto> getAvailableRooms() {
        return repository.findByIsOccupiedFalse().stream().map(mapper::toDto).toList();
    }
}
