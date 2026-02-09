package mapper.stay;

import dto.stay.StayDto;
import model.Room;
import model.Stay;
import model.StayRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface StayMapper {
    @Mapping(target = "stayRequestId", source = "stayRequest.id")
    @Mapping(target = "roomId", source = "room.id")
    StayDto toDto(Stay entity);

    @Mapping(target = "stayRequest", source = "stayRequestId", qualifiedByName = "stayRequestFromId")
    @Mapping(target = "room", source = "roomId", qualifiedByName = "roomFromId")
    Stay toEntity(StayDto dto);

    @Named("stayRequestFromId")
    default StayRequest stayRequestFromId(Long id) {
        if (id == null) {
            return null;
        }
        StayRequest stayRequest = new StayRequest();
        stayRequest.setId(id);
        return stayRequest;
    }

    @Named("roomFromId")
    default Room roomFromId(Long id) {
        if (id == null) {
            return null;
        }
        Room room = new Room();
        room.setId(id);
        return room;
    }
}
