package com.example.repositories;

import java.util.List;
import com.example.models.RequestStatus;
import com.example.models.StayRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StayRequestRepository extends JpaRepository<StayRequest, Long> {
    List<StayRequest> findByPatientId(Long patientId);

    List<StayRequest> findByStatus(RequestStatus status);

    @Query(value = "select approve_stay_request(:requestId, :roomId, :doctorId)", nativeQuery = true)
    Long approveStayRequest(
            @Param("requestId") Long requestId,
            @Param("roomId") Long roomId,
            @Param("doctorId") Long doctorId);

    @Modifying
    @Query(value = "update Stay_request set status = 'REJECTED' where id = :requestId", nativeQuery = true)
    int rejectStayRequest(@Param("requestId") Long requestId);
}
