package repository;

import java.util.List;
import model.RequestStatus;
import model.StayRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StayRequestRepository extends JpaRepository<StayRequest, Long> {
    List<StayRequest> findByPatientId(Long patientId);

    List<StayRequest> findByStatus(RequestStatus status);
}
