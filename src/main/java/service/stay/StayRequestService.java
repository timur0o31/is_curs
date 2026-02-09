package service.stay;

import dto.stay.StayRequestDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mapper.stay.StayRequestMapper;
import model.RequestStatus;
import model.RequestType;
import model.StayRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.stay.StayRequestRepository;

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

    public StayRequestDto createCheckInRequest(Long patientId, java.time.LocalDate admissionDate, java.time.LocalDate dischargeDate) {
        StayRequest entity = new StayRequest();
        entity.setPatientId(patientId);
        entity.setAdmissionDate(admissionDate);
        entity.setDischargeDate(dischargeDate);
        entity.setStatus(RequestStatus.PENDING);
        entity.setType(RequestType.CHECK_IN);
        return mapper.toDto(repository.save(entity));
    }

    public StayRequestDto createExpansionRequest(Long patientId, java.time.LocalDate admissionDate, java.time.LocalDate dischargeDate) {
        StayRequest entity = new StayRequest();
        entity.setPatientId(patientId);
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
        return repository.findByPatientId(patientId).stream().map(mapper::toDto).toList();
    }

    public List<StayRequestDto> getByStatus(RequestStatus status) {
        return repository.findByStatus(status).stream().map(mapper::toDto).toList();
    }

    public Long approveRequest(Long requestId, Long roomId, Long doctorId) {
        return repository.approveStayRequest(requestId, roomId, doctorId);
    }

    @Transactional
    public void rejectRequest(Long requestId) {
        int updated = repository.rejectStayRequest(requestId);
        if (updated == 0) {
            throw new IllegalArgumentException("StayRequest not found: " + requestId);
        }
    }
}
