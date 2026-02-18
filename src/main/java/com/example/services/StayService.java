package com.example.services;

import com.example.dto.ActivePatientDto;
import com.example.dto.StayDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.models.StayRequest;
import com.example.repositories.DoctorRepository;
import com.example.repositories.StayRequestRepository;
import lombok.RequiredArgsConstructor;
import com.example.mapper.StayMapper;
import com.example.models.Stay;
import org.springframework.stereotype.Service;
import com.example.repositories.StayRepository;

@Service
@RequiredArgsConstructor
public class StayService {
    private final StayRepository repository;
    private final StayMapper mapper;
    private final DoctorRepository doctorRepository;
    private final StayRequestRepository stayRequestRepository;
    public List<StayDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public StayDto getById(Long id) {
        Stay entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Stay not found: " + id));
        return mapper.toDto(entity);
    }

    public StayDto create(StayDto dto) {
        Stay entity = mapper.toEntity(dto);
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    public StayDto update(Long id, StayDto dto) {
        Stay entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<StayDto> getByDoctorId(Long doctorId) {
        return repository.findByDoctorId(doctorId).stream().map(mapper::toDto).toList();
    }
    public List<ActivePatientDto> getPatientsByDoctorId(Long userId){
        Long doctorId = doctorRepository.findByUser_Id(userId).get().getId();
        ArrayList<ActivePatientDto> names = new ArrayList<>();
        List<Stay> stay = repository.findByDoctorId(doctorId);
        for (Stay stay1 : stay) {
            names.add(new ActivePatientDto(stay1.getStayRequest().getPatient().getId(),stay1.getId(),stay1.getStayRequest().getPatient().getUser().getName()));
        }
        return names;
    }

//    public StayDto getByStayRequestID(Long stayRequestId) {
//        Stay entity = repository.findByStay_Request_Id(stayRequestId)
//                .orElseThrow(() -> new IllegalArgumentException("Stay not found for StayRequest ID: " + stayRequestId));
//        return mapper.toDto(entity);
//    }

    public List<StayDto> getByRoomId(Long roomId) {
        return repository.findByRoom_Id(roomId).stream().map(mapper::toDto).toList();
    }

    public Stay getActiveStayByPatientId(Long patientId) {
        LocalDate today = LocalDate.now();
        StayRequest stayRequest = stayRequestRepository.findActiveStayRequestByPatientId(patientId, today)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Active stay request not found for patient: " + patientId));
        return repository.findByStayRequestId(stayRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Stay not found for stay request: " + stayRequest.getId()));
    }
    public Long discharge(Long stayId, LocalDate dischargeDate) {
        if (dischargeDate == null) {
            return repository.dischargeStay(stayId);
        }
        return repository.dischargeStayEarly(stayId, dischargeDate);
    }
}
