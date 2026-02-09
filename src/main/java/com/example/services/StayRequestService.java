package com.example.services;

import com.example.dto.StayRequestDto;
import java.util.List;

import com.example.models.*;
import com.example.repositories.PatientRepository;
import com.example.repositories.StayRepository;
import lombok.RequiredArgsConstructor;
import com.example.mapper.StayRequestMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.repositories.StayRequestRepository;

@Service
@RequiredArgsConstructor
public class StayRequestService {
    private final StayRequestRepository repository;
    private final StayRequestMapper mapper;
    private final UserService userService;
    private final PatientRepository patientRepository;
    private final StayRepository stayRepository;
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

    public StayRequestDto createCheckInRequest(String email, java.time.LocalDate admissionDate, java.time.LocalDate dischargeDate) {
        User user = userService.findByEmail(email).orElseThrow();
        Patient patient = patientRepository.findByUser_Id(user.getId()).orElseThrow();
        StayRequest entity = new StayRequest();
        entity.setPatient(patient);
        entity.setAdmissionDate(admissionDate);
        entity.setDischargeDate(dischargeDate);
        entity.setStatus(RequestStatus.PENDING);
        entity.setType(RequestType.CHECK_IN);
        return mapper.toDto(repository.save(entity));
    }

    public StayRequestDto createExpansionRequest(String email, java.time.LocalDate admissionDate, java.time.LocalDate dischargeDate) {
        User user = userService.findByEmail(email).orElseThrow();
        Patient patient = patientRepository.findByUser_Id(user.getId()).orElseThrow();
        StayRequest entity = new StayRequest();
        entity.setPatient(patient);
        entity.setAdmissionDate(admissionDate);
        entity.setDischargeDate(dischargeDate);
        entity.setStatus(RequestStatus.PENDING);
        entity.setType(RequestType.EXPANSION);
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
        return repository.findByPatient_Id(patientId).stream().map(mapper::toDto).toList();
    }

    public List<StayRequestDto> getByStatus(RequestStatus status) {
        return repository.findByStatus(status).stream().map(mapper::toDto).toList();
    }

    public Long approveRequest(Long requestId, Long roomId, Long doctorId) {
        return repository.approveStayRequest(requestId, roomId, doctorId);
    }

    @Transactional
    public StayRequestDto approveExpansion(Long requestId) {
        StayRequest expansion = repository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("StayRequest not found: " + requestId));
        if (expansion.getType() != RequestType.EXPANSION) {
            throw new IllegalArgumentException("StayRequest is not an expansion: " + requestId);
        }
        if (expansion.getStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("StayRequest is not pending: " + requestId);
        }

        Long patientId = expansion.getPatient().getId();
        Stay stay = stayRepository
                .findFirstByStayRequest_Patient_IdAndStayRequest_TypeAndStayRequest_StatusOrderByIdDesc(
                        patientId, RequestType.CHECK_IN, RequestStatus.APPROVED)
                .orElseThrow(() -> new IllegalStateException("Active stay not found for patient: " + patientId));

        StayRequest firstStayRequest = stay.getStayRequest();
        firstStayRequest.setDischargeDate(expansion.getDischargeDate());
        expansion.setStatus(RequestStatus.APPROVED);

        repository.save(firstStayRequest);
        return mapper.toDto(repository.save(expansion));
    }
    @Transactional
    public void rejectRequest(Long requestId) {
        int updated = repository.rejectStayRequest(requestId);
        if (updated == 0) {
            throw new IllegalArgumentException("StayRequest not found: " + requestId);
        }
    }
}
