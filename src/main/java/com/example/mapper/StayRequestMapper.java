package com.example.mapper;

import com.example.dto.StayRequestDto;
import com.example.models.StayRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StayRequestMapper {
    StayRequestDto toDto(StayRequest entity);

    StayRequest toEntity(StayRequestDto dto);
}
