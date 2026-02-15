package com.example.repositories;

import com.example.models.Session;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByDoctorId(Long doctorId);

    List<Session> findByProcedure_Id(Long procedureId);

    List<Session> findBySessionDateGreaterThanEqualAndProcedure_IsOptionalTrueAndProcedure_DefaultSeatsGreaterThanEqual(
            LocalDate date,
            Integer defaultSeats,
            Sort sort);
}
