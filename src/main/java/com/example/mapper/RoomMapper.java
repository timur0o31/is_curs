package mapper;

import dto.RoomDto;
import model.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomDto toDto(Room entity);

    Room toEntity(RoomDto dto);
}
