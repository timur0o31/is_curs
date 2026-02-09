package com.example.repositories;

import java.util.List;
import com.example.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByIsOccupiedFalse();
}
