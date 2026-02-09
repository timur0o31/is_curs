package com.example.repositories;

import java.util.List;
import com.example.models.Stay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StayRepository extends JpaRepository<Stay, Long> {
    List<Stay> findByDoctorId(Long doctorId);

    List<Stay> findByRoom_Id(Long roomId);
}
