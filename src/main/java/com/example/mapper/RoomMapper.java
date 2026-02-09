package com.example.mapper;

import com.example.dto.RoomDto;
import com.example.models.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomDto toDto(Room entity);

    Room toEntity(RoomDto dto);
}
