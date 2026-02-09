package com.example.services;

import com.example.dto.LockerDto;
import com.example.mapper.LockerMapper;
import com.example.models.Locker;
import com.example.repositories.LockerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LockerService {
    private final LockerRepository repository;
    private final LockerMapper mapper;

    public List<LockerDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public LockerDto getById(Long id) {
        Locker entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Locker not found: " + id));
        return mapper.toDto(entity);
    }

    public LockerDto create(LockerDto dto) {
        Locker entity = mapper.toEntity(dto);
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    public LockerDto update(Long id, LockerDto dto) {
        Locker entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public LockerDto getByPatientId(Long patientId) {
        Locker entity = repository.findByPatientId(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Locker not found for patient: " + patientId));
        return mapper.toDto(entity);
    }

    public List<LockerDto> getAvailableLockers() {
        return repository.findByPatientIdIsNull().stream().map(mapper::toDto).toList();
    }

    /**
     * Закрепить шкафчик за пациентом.
     * - У пациента не может быть больше одного шкафчика.
     * - Нельзя занять уже занятый другим пациентом шкафчик.
     */
    @Transactional
    public LockerDto assignLockerToPatient(Long lockerId, Long patientId) {
        // Проверяем, что у пациента ещё нет шкафчика
        repository.findByPatientId(patientId).ifPresent(existing -> {
            if (!existing.getId().equals(lockerId)) {
                throw new IllegalStateException("Пациент уже имеет закрепленный шкафчик: " + existing.getLockerNumber());
            }
        });

        Locker locker = repository.findById(lockerId)
                .orElseThrow(() -> new IllegalArgumentException("Locker not found: " + lockerId));

        // Проверяем, что шкафчик свободен или уже принадлежит этому же пациенту
        if (locker.getPatientId() != null && !locker.getPatientId().equals(patientId)) {
            throw new IllegalStateException("Шкафчик уже закреплён за другим пациентом");
        }

        locker.setPatientId(patientId);
        return mapper.toDto(repository.save(locker));
    }

    /**
     * Открепить шкафчик от пациента (пациент остаётся без шкафчика).
     */
    @Transactional
    public void unassignLockerFromPatient(Long patientId) {
        Locker locker = repository.findByPatientId(patientId)
                .orElseThrow(() -> new IllegalArgumentException("У пациента нет закрепленного шкафчика"));
        locker.setPatientId(null);
        repository.save(locker);
    }
}
