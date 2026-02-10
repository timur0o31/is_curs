package com.example.services;

import com.example.dto.SeatDto;
import com.example.mapper.SeatMapper;
import com.example.models.Diet;
import com.example.models.DiningTable;
import com.example.models.MedicalCard;
import com.example.models.Seat;
import com.example.repositories.DiningTableRepository;
import com.example.repositories.MedicalCardRepository;
import com.example.repositories.SeatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository repository;
    private final DiningTableRepository diningTableRepository;
    private final MedicalCardRepository medicalCardRepository;
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


    @Transactional
    public SeatDto assignSeatForPatientByDiet(Long patientId, Diet diet) {
        List<Seat> currentSeats = repository.findByPatientId(patientId);
        for (Seat seat : currentSeats) {
            seat.setPatientId(null);
            seat.setIsOccupied(false);
        }
        repository.saveAll(currentSeats);

        List<DiningTable> tables = diningTableRepository.findByDiet(diet);
        for (DiningTable table : tables) {
            List<Seat> seats = repository.findByDiningTable_Id(table.getId());
            for (Seat seat : seats) {
                if (seat.getPatientId() == null && !seat.getIsOccupied()) {
                    seat.setPatientId(patientId);
                    seat.setIsOccupied(true);
                    return mapper.toDto(repository.save(seat));
                }
            }
        }

        throw new IllegalStateException("Нет свободных мест для диеты: " + diet);
    }


    @Transactional
    public SeatDto bookSeat(Long patientId, Long seatId) {
        MedicalCard medicalCard = medicalCardRepository.findByPatientId(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Медицинская карта не найдена для пациента: " + patientId));

        Diet patientDiet = medicalCard.getDiet();
        if (patientDiet == null) {
            throw new IllegalStateException("У пациента не назначена диета");
        }

        Seat seat = repository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("Место не найдено: " + seatId));

        if (seat.getIsOccupied()) {
            throw new IllegalStateException("Место уже занято");
        }

        Diet tableDiet = seat.getDiningTable().getDiet();
        if (!patientDiet.equals(tableDiet)) {
            throw new IllegalStateException(
                    String.format("Диета пациента (%s) не совпадает с диетой столика (%s)",
                            patientDiet, tableDiet));
        }

        List<Seat> currentSeats = repository.findByPatientId(patientId);
        for (Seat currentSeat : currentSeats) {
            currentSeat.setPatientId(null);
            currentSeat.setIsOccupied(false);
        }
        repository.saveAll(currentSeats);

        seat.setPatientId(patientId);
        seat.setIsOccupied(true);
        return mapper.toDto(repository.save(seat));
    }

    @Transactional
    public void releaseSeat(Long patientId) {
        List<Seat> currentSeats = repository.findByPatientId(patientId);
        for (Seat seat : currentSeats) {
            seat.setPatientId(null);
            seat.setIsOccupied(false);
        }
        repository.saveAll(currentSeats);
    }

    public List<SeatDto> getAvailableSeatsForPatient(Long patientId) {
        MedicalCard medicalCard = medicalCardRepository.findByPatientId(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Медицинская карта не найдена для пациента: " + patientId));

        Diet patientDiet = medicalCard.getDiet();
        if (patientDiet == null) {
            throw new IllegalStateException("У пациента не назначена диета");
        }

        List<DiningTable> tables = diningTableRepository.findByDiet(patientDiet);

        return tables.stream()
                .flatMap(table -> repository.findAvailableSeatsByDiningTableId(table.getId()).stream())
                .map(mapper::toDto)
                .toList();
    }
}
