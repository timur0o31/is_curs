package com.example.mapper;

import com.example.dto.ProcedureDto;
import com.example.models.Procedure;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProcedureMapper {
    ProcedureDto toDto(Procedure entity);

    Procedure toEntity(ProcedureDto dto);
}
