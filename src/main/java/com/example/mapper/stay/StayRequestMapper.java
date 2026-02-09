package mapper.stay;

import dto.stay.StayRequestDto;
import model.StayRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StayRequestMapper {
    StayRequestDto toDto(StayRequest entity);

    StayRequest toEntity(StayRequestDto dto);
}
