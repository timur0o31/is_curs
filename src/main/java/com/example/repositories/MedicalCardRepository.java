package com.example.repositories;

import java.util.Optional;
import com.example.models.MedicalCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalCardRepository extends JpaRepository<MedicalCard, Long> {
    Optional<MedicalCard> findByPatientId(Long patientId);
}
