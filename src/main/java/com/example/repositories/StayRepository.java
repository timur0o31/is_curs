package com.example.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.example.models.RequestStatus;
import com.example.models.RequestType;
import com.example.models.Stay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StayRepository extends JpaRepository<Stay, Long> {
    List<Stay> findByDoctorId(Long doctorId);

    List<Stay> findByRoom_Id(Long roomId);

    Optional<Stay> findByStayRequestId(Long stayRequestId);

    Optional<Stay> findFirstByStayRequest_Patient_IdAndStayRequest_TypeAndStayRequest_StatusOrderByIdDesc(
            Long patientId,
            RequestType type,
            RequestStatus status
    );
    @Query(value = "select discharge_stay(:stayId)", nativeQuery = true)
    Long dischargeStay(@Param("stayId") Long stayId);

    @Query(value = "select discharge_stay_early(:stayId, :dischargeDate)", nativeQuery = true)
    Long dischargeStayEarly(
            @Param("stayId") Long stayId,
            @Param("dischargeDate") LocalDate dischargeDate
    );
}
