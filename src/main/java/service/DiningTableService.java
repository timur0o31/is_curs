package service;

import dto.DiningTableDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mapper.DiningTableMapper;
import model.Diet;
import model.DiningTable;
import org.springframework.stereotype.Service;
import repository.DiningTableRepository;

@Service
@RequiredArgsConstructor
public class DiningTableService {
    private final DiningTableRepository repository;
    private final DiningTableMapper mapper;

    public List<DiningTableDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public DiningTableDto getById(Long id) {
        DiningTable entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("DiningTable not found: " + id));
        return mapper.toDto(entity);
    }

    public DiningTableDto create(DiningTableDto dto) {
        DiningTable entity = mapper.toEntity(dto);
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    public DiningTableDto update(Long id, DiningTableDto dto) {
        DiningTable entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<DiningTableDto> getByDiet(Diet diet) {
        return repository.findByDiet(diet).stream().map(mapper::toDto).toList();
    }
}
