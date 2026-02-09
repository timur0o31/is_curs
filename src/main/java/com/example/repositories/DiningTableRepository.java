package com.example.repositories;

import java.util.List;
import com.example.models.DiningTable;
import com.example.models.Diet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiningTableRepository extends JpaRepository<DiningTable, Long> {
    List<DiningTable> findByDiet(Diet diet);
}
