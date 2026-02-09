package com.example.mapper;

import com.example.dto.MedicamentDto;
import com.example.models.Medicament;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicamentMapper {
    MedicamentDto toDto(Medicament entity);

    Medicament toEntity(MedicamentDto dto);
}
