package com.example.repositories;

import java.util.List;

import com.example.dto.NotificationDto;
import com.example.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUser_IdOrderByCreatedAtDesc(Long userId);

    List<Notification> findByUser_IdAndReadIsFalseOrderByCreatedAtDesc(Long userId);
}

