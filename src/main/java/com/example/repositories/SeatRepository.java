package repository;

import java.util.List;
import model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByPatientId(Long patientId);

    List<Seat> findByDiningTable_Id(Long diningTableId);
}
