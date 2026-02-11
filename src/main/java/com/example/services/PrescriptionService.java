package com.example.services;

import com.example.dto.PrescriptionDto;
import java.util.List;
import java.util.Objects;

import com.example.models.MedicalCard;
import com.example.models.Stay;
import com.example.models.StayRequest;
import com.example.repositories.*;
import lombok.RequiredArgsConstructor;
import com.example.mapper.PrescriptionMapper;
import com.example.models.Prescription;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrescriptionService {
    private final PrescriptionRepository repository;
    private final PrescriptionMapper mapper;
    private final StayRepository stayRepository;
    private final StayRequestService stayRequestService;
    private final MedicalCardRepository medicalCardRepository;
    private final DoctorRepository doctorRepository;

    public List<PrescriptionDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public PrescriptionDto getById(Long id) {
        Prescription entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prescription not found: " + id));
        return mapper.toDto(entity);
    }

    public PrescriptionDto create(PrescriptionDto dto) {
        Prescription entity = mapper.toEntity(dto);
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    public PrescriptionDto update(Long id, PrescriptionDto dto) {
        Prescription entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDto(repository.save(entity));
    }


    public PrescriptionDto prescribe(Long doctorUserId, Long patientId, PrescriptionDto dto) {

        MedicalCard medicalCard = medicalCardRepository
                .findByPatientId(patientId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Patient has no medical card"));

        StayRequest stayRequest = stayRequestService.getActiveStayRequest(patientId)
                .orElseThrow(() ->
                        new IllegalStateException("Patient is not admitted to the hospital"));

        Stay stay = stayRepository.findByStayRequestId(stayRequest.getId())
                .orElseThrow(() ->
                        new IllegalStateException("No stay found for the active stay request"));

        Long doctorId = doctorRepository.findById(stay.getDoctorId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Doctor not found for user ID: " + doctorUserId))
                .getUser().getId();

        if (!Objects.equals(doctorUserId, doctorId)) {
            throw new IllegalStateException("Вы не являетесь лечащим врачом этого пациента");
        }

        if (dto.getEndDate() != null &&
                dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new IllegalArgumentException("End date before start date");
        }

        dto.setMedicalCardId(medicalCard.getId());

        Prescription prescription = repository.save(mapper.toEntity(dto));

        return mapper.toDto(prescription);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<PrescriptionDto> getByMedicalCardId(Long medicalCardId) {
        return repository.findByMedicalCard_Id(medicalCardId).stream().map(mapper::toDto).toList();
    }

    public List<PrescriptionDto> getByMedicamentId(Long medicamentId) {
        return repository.findByMedicament_Id(medicamentId).stream().map(mapper::toDto).toList();
    }
}
