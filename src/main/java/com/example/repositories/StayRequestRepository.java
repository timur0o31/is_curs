package com.example.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.example.models.RequestStatus;
import com.example.models.Stay;
import com.example.models.StayRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StayRequestRepository extends JpaRepository<StayRequest, Long> {
    List<StayRequest> findByPatient_Id(Long patientId);

    List<StayRequest> findByStatus(RequestStatus status);

    /**
     * Находит активный (одобренный) StayRequest для пациента на текущую дату.
     * Возвращает запрос, где текущая дата находится между датами заезда и выезда,
     * и статус = APPROVED.
     *
     * @param patientId ID пациента
     * @param currentDate текущая дата для проверки
     * @return Optional с активным StayRequest или пустой Optional
     */
    @Query("SELECT sr FROM StayRequest sr " +
           "WHERE sr.patient.id = :patientId " +
           "AND sr.status = 'APPROVED' " +
           "AND sr.admissionDate <= :currentDate " +
           "AND sr.dischargeDate >= :currentDate")
    Optional<StayRequest> findActiveStayRequestByPatientId(
            @Param("patientId") Long patientId,
            @Param("currentDate") LocalDate currentDate);

    @Query(value = "select approve_stay_request(:requestId, :roomId, :doctorId)", nativeQuery = true)
    Long approveStayRequest(
            @Param("requestId") Long requestId,
            @Param("roomId") Long roomId,
            @Param("doctorId") Long doctorId);

    @Modifying
    @Query(value = "update Stay_request set status = 'REJECTED' where id = :requestId", nativeQuery = true)
    int rejectStayRequest(@Param("requestId") Long requestId);
    List<StayRequest> findByPatient_User_Id(Long userId);
}
