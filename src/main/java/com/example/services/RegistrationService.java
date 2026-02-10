package com.example.services;

import com.example.dto.RegistrationDto;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.example.mapper.RegistrationMapper;
import com.example.models.Doctor;
import com.example.models.Registration;
import com.example.models.Registration.RegistrationId;
import com.example.models.RequestStatus;
import com.example.models.Session;
import com.example.models.Stay;
import com.example.models.StayRequest;
import com.example.models.User;
import org.springframework.stereotype.Service;
import com.example.repositories.DoctorRepository;
import com.example.repositories.RegistrationRepository;
import com.example.repositories.SessionRepository;
import com.example.repositories.StayRepository;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationRepository repository;
    private final RegistrationMapper mapper;
    private final UserService userService;
    private final DoctorRepository doctorRepository;
    private final StayRepository stayRepository;
    private final SessionRepository sessionRepository;

    public List<RegistrationDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public RegistrationDto getById(Long stayId, Long sessionId) {
        RegistrationId id = new RegistrationId(stayId, sessionId);
        Registration entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registration not found: " + stayId + "/" + sessionId));
        return mapper.toDto(entity);
    }

    public RegistrationDto create(RegistrationDto dto) {
        if (Boolean.TRUE.equals(dto.getIsNecessary())) {
            throw new IllegalArgumentException("Обязательные процедуры назначает лечащий врач");
        }

        Registration entity = mapper.toEntity(dto);
        validateRegistration(entity);

        // UC-8 / UC-9: проверка, что пациент не записан на тот же сеанс дважды
        ensureNotDuplicate(entity);

        return mapper.toDto(repository.save(entity));
    }

    public RegistrationDto createMandatoryForDoctor(String email, RegistrationDto dto) {
        dto.setIsNecessary(true);

        Registration entity = mapper.toEntity(dto);
        RegistrationContext context = validateRegistration(entity);

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
        Doctor doctor = doctorRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found for user: " + email));

        if (context.stay.getDoctorId() == null || !context.stay.getDoctorId().equals(doctor.getId())) {
            throw new IllegalArgumentException("Врач не является лечащим для данного пациента");
        }

        ensureNotDuplicate(entity);
        return mapper.toDto(repository.save(entity));
    }

    public RegistrationDto createMandatoryByAdmin(RegistrationDto dto) {
        dto.setIsNecessary(true);

        Registration entity = mapper.toEntity(dto);
        validateRegistration(entity);
        ensureNotDuplicate(entity);

        return mapper.toDto(repository.save(entity));
    }

    public RegistrationDto update(Long stayId, Long sessionId, RegistrationDto dto) {
        Registration entity = mapper.toEntity(dto);
        entity.setId(new RegistrationId(stayId, sessionId));
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long stayId, Long sessionId) {
        repository.deleteById(new RegistrationId(stayId, sessionId));
    }

    public List<RegistrationDto> getByStayId(Long stayId) {
        return repository.findByStay_Id(stayId).stream().map(mapper::toDto).toList();
    }

    public List<RegistrationDto> getBySessionId(Long sessionId) {
        return repository.findBySession_Id(sessionId).stream().map(mapper::toDto).toList();
    }

    private void ensureNotDuplicate(Registration entity) {
        if (entity.getStay() == null || entity.getSession() == null) {
            return;
        }
        RegistrationId id = new RegistrationId(
                entity.getStay().getId(),
                entity.getSession().getId()
        );
        if (repository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Пациент уже записан на данный сеанс: stayId="
                            + id.getStayId() + ", sessionId=" + id.getSessionId()
            );
        }
    }

    private RegistrationContext validateRegistration(Registration entity) {
        if (entity.getStay() == null || entity.getStay().getId() == null) {
            throw new IllegalArgumentException("stayId is required");
        }
        if (entity.getSession() == null || entity.getSession().getId() == null) {
            throw new IllegalArgumentException("sessionId is required");
        }

        Stay stay = stayRepository.findById(entity.getStay().getId())
                .orElseThrow(() -> new IllegalArgumentException("Stay not found: " + entity.getStay().getId()));
        Session session = sessionRepository.findById(entity.getSession().getId())
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + entity.getSession().getId()));

        StayRequest stayRequest = stay.getStayRequest();
        if (stayRequest.getStatus() != RequestStatus.APPROVED) {
            throw new IllegalArgumentException("Проживание не активно");
        }

        LocalDate sessionDate = session.getSessionDate();
        if (sessionDate != null) {
            if (sessionDate.isBefore(stayRequest.getAdmissionDate())
                    || sessionDate.isAfter(stayRequest.getDischargeDate())) {
                throw new IllegalArgumentException("Сеанс вне периода проживания");
            }
        }

        if (session.getProcedure() != null && session.getProcedure().getDefaultSeats() != null) {
            long current = repository.countBySession_Id(session.getId());
            if (current >= session.getProcedure().getDefaultSeats()) {
                throw new IllegalArgumentException("На сеанс уже записано максимальное число пациентов");
            }
        }

        return new RegistrationContext(stay, session);
    }

    private record RegistrationContext(Stay stay, Session session) {}
}
