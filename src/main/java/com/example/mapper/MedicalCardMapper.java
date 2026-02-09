package com.example.mapper;

import com.example.dto.MedicalCardDto;
import com.example.models.MedicalCard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicalCardMapper {
    MedicalCardDto toDto(MedicalCard entity);

    MedicalCard toEntity(MedicalCardDto dto);
}
