package repository;

import java.util.List;
import java.util.Optional;
import model.Locker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LockerRepository extends JpaRepository<Locker, Long> {
    Optional<Locker> findByPatientId(Long patientId);

    List<Locker> findByPatientIdIsNull();
}
