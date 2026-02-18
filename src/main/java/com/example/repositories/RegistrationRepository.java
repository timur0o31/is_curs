package com.example.repositories;

import java.util.List;
import com.example.models.Registration;
import com.example.models.Registration.RegistrationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RegistrationRepository extends JpaRepository<Registration, RegistrationId> {
    List<Registration> findByStay_Id(Long stayId);

    List<Registration> findBySession_Id(Long sessionId);

    long countBySession_Id(Long sessionId);

    @Query("select r from Registration r " +
           "join fetch r.session s " +
           "left join fetch s.procedure " +
           "where r.stay.id = :stayId")
    List<Registration> findWithSessionAndProcedureByStayId(@Param("stayId") Long stayId);
}
