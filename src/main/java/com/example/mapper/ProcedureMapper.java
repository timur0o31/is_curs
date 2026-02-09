package mapper;

import dto.ProcedureDto;
import model.Procedure;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProcedureMapper {
    ProcedureDto toDto(Procedure entity);

    Procedure toEntity(ProcedureDto dto);
}
