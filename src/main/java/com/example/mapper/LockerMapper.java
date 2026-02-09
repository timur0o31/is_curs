package com.example.mapper;

import com.example.dto.LockerDto;
import com.example.models.Locker;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LockerMapper {
    LockerDto toDto(Locker entity);

    Locker toEntity(LockerDto dto);
}
