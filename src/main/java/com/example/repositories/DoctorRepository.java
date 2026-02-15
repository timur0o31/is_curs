package com.example.repositories;

import com.example.models.Doctor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUser_Id(Long userId);

    List<Doctor> findByWorkingTrue();
}
