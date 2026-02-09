package com.example.repositories;

import java.util.List;
import com.example.models.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcedureRepository extends JpaRepository<Procedure, Long> {
    List<Procedure> findByNameContainingIgnoreCase(String name);
}
