package com.example.mapper;

import com.example.dto.DiningTableDto;
import com.example.models.DiningTable;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiningTableMapper {
    DiningTableDto toDto(DiningTable entity);

    DiningTable toEntity(DiningTableDto dto);
}
