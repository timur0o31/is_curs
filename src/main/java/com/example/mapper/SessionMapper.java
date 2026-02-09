package com.example.mapper;

import com.example.dto.SessionDto;
import com.example.models.Procedure;
import com.example.models.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SessionMapper {
    @Mapping(target = "procedureId", source = "procedure.id")
    SessionDto toDto(Session entity);

    @Mapping(target = "procedure", source = "procedureId", qualifiedByName = "procedureFromId")
    Session toEntity(SessionDto dto);

    @Named("procedureFromId")
    default Procedure procedureFromId(Long id) {
        if (id == null) {
            return null;
        }
        Procedure procedure = new Procedure();
        procedure.setId(id);
        return procedure;
    }
}
