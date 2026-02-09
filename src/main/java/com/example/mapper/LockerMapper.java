package mapper;

import dto.LockerDto;
import model.Locker;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LockerMapper {
    LockerDto toDto(Locker entity);

    Locker toEntity(LockerDto dto);
}
