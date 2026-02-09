package com.example.repositories;

import java.util.List;
import com.example.models.Registration;
import com.example.models.Registration.RegistrationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, RegistrationId> {
    List<Registration> findByStay_Id(Long stayId);

    List<Registration> findBySession_Id(Long sessionId);
}
