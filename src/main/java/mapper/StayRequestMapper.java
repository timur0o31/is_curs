package mapper;

import dto.StayRequestDto;
import model.StayRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StayRequestMapper {
    StayRequestDto toDto(StayRequest entity);

    StayRequest toEntity(StayRequestDto dto);
}
