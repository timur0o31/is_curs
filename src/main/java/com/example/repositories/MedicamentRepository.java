package com.example.repositories;

import java.util.List;
import com.example.models.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentRepository extends JpaRepository<Medicament, Long> {
    List<Medicament> findByNameContainingIgnoreCase(String name);
}
