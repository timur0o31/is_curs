package com.example.repositories;

import com.example.models.Patient;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUser_Id(Long userId);
}
