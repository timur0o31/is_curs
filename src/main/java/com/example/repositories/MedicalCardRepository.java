package repository;

import java.util.Optional;
import model.MedicalCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalCardRepository extends JpaRepository<MedicalCard, Long> {
    Optional<MedicalCard> findByPatientId(Long patientId);
}
