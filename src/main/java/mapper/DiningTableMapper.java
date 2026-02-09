package mapper;

import dto.DiningTableDto;
import model.DiningTable;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiningTableMapper {
    DiningTableDto toDto(DiningTable entity);

    DiningTable toEntity(DiningTableDto dto);
}
