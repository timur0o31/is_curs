package com.example.services;

import com.example.dto.SeatDto;
import com.example.mapper.SeatMapper;
import com.example.models.Diet;
import com.example.models.DiningTable;
import com.example.models.Seat;
import com.example.repositories.DiningTableRepository;
import com.example.repositories.SeatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository repository;
    private final DiningTableRepository diningTableRepository;
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

    /**
     * Назначает пациенту место в столовой согласно его диете.
     * Если у пациента уже было место, оно освобождается.
     */
    public SeatDto assignSeatForPatientByDiet(Long patientId, Diet diet) {
        // Освобождаем все текущие места пациента
        List<Seat> currentSeats = repository.findByPatientId(patientId);
        for (Seat seat : currentSeats) {
            seat.setPatientId(null);
        }
        repository.saveAll(currentSeats);

        // Ищем свободное место за столом с нужной диетой
        List<DiningTable> tables = diningTableRepository.findByDiet(diet);
        for (DiningTable table : tables) {
            List<Seat> seats = repository.findByDiningTable_Id(table.getId());
            for (Seat seat : seats) {
                if (seat.getPatientId() == null) {
                    seat.setPatientId(patientId);
                    return mapper.toDto(repository.save(seat));
                }
            }
        }

        throw new IllegalStateException("Нет свободных мест для диеты: " + diet);
    }
}
