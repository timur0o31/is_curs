package com.example.repositories;

import java.util.List;
import com.example.models.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByMedicalCard_Id(Long medicalCardId);

    List<Prescription> findByMedicament_Id(Long medicamentId);
}
