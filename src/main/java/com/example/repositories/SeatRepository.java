package com.example.repositories;

import java.util.List;
import com.example.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByPatientId(Long patientId);

    List<Seat> findByDiningTable_Id(Long diningTableId);

    @Query("SELECT s FROM Seat s WHERE s.diningTable.id = :diningTableId AND s.isOccupied = false")
    List<Seat> findAvailableSeatsByDiningTableId(@Param("diningTableId") Long diningTableId);
}
