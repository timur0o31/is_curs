package com.example.services;

import com.example.dto.PublicDoctorDto;
import com.example.dto.PublicEventDto;
import com.example.dto.PublicHomeDto;
import com.example.dto.PublicProcedureDto;
import com.example.models.Doctor;
import com.example.models.Procedure;
import com.example.models.Session;
import com.example.models.User;
import com.example.repositories.DoctorRepository;
import com.example.repositories.ProcedureRepository;
import com.example.repositories.SessionRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicHomeService {

    private static final int EVENT_MIN_SEATS = 30;

    private final ProcedureRepository procedureRepository;
    private final DoctorRepository doctorRepository;
    private final SessionRepository sessionRepository;

    public List<PublicProcedureDto> getProcedures() {
        return procedureRepository.findAll().stream()
                .map(this::toPublicProcedureDto)
                .toList();
    }

    public List<PublicDoctorDto> getDoctors() {
        return doctorRepository.findByWorkingTrue().stream()
                .map(this::toPublicDoctorDto)
                .toList();
    }

    public List<PublicEventDto> getEvents() {
        LocalDate today = LocalDate.now();
        return sessionRepository.findBySessionDateGreaterThanEqualAndProcedure_IsOptionalTrueAndProcedure_DefaultSeatsGreaterThanEqual(
                        today,
                        EVENT_MIN_SEATS,
                        Sort.by(Sort.Order.asc("sessionDate"), Sort.Order.asc("timeStart")))
                .stream()
                .map(this::toPublicEventDto)
                .toList();
    }

    public PublicHomeDto getHome() {
        return new PublicHomeDto(
                getProcedures(),
                getDoctors(),
                getEvents()
        );
    }

    private PublicProcedureDto toPublicProcedureDto(Procedure procedure) {
        return new PublicProcedureDto(
                procedure.getId(),
                procedure.getName(),
                trimDescription(procedure.getDescription())
        );
    }

    private PublicDoctorDto toPublicDoctorDto(Doctor doctor) {
        return new PublicDoctorDto(
                doctor.getId(),
                resolveDoctorName(doctor),
                doctor.getSpecialization()
        );
    }

    private PublicEventDto toPublicEventDto(Session session) {
        Procedure procedure = session.getProcedure();
        String title = procedure != null && procedure.getName() != null && !procedure.getName().isBlank()
                ? procedure.getName().trim()
                : "Событие";
        String description = procedure != null ? procedure.getDescription() : null;

        return new PublicEventDto(
                session.getId(),
                title,
                session.getSessionDate(),
                session.getTimeStart(),
                (description == null || description.isBlank())
                        ? "Открытое мероприятие санатория"
                        : description.trim()
        );
    }

    private String resolveDoctorName(Doctor doctor) {
        if (doctor == null) {
            return "Врач санатория";
        }

        User user = doctor.getUser();
        if (user == null) {
            return "Врач санатория";
        }

        if (user.getName() != null && !user.getName().isBlank()) {
            return user.getName().trim();
        }

        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            return user.getEmail().trim();
        }

        return "Врач санатория";
    }

    private String trimDescription(String description) {
        if (description == null) {
            return null;
        }

        String trimmed = description.trim();
        if (trimmed.length() <= 120) {
            return trimmed;
        }
        return trimmed.substring(0, 117) + "...";
    }
}
