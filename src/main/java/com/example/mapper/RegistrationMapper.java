package com.example.mapper;

import com.example.dto.RegistrationDto;
import com.example.models.Registration;
import com.example.models.Session;
import com.example.models.Stay;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RegistrationMapper {
    @Mapping(target = "stayId", source = "stay.id")
    @Mapping(target = "sessionId", source = "session.id")
    RegistrationDto toDto(Registration entity);

    @Mapping(target = "id.stayId", source = "stayId")
    @Mapping(target = "id.sessionId", source = "sessionId")
    @Mapping(target = "stay", source = "stayId", qualifiedByName = "stayFromId")
    @Mapping(target = "session", source = "sessionId", qualifiedByName = "sessionFromId")
    Registration toEntity(RegistrationDto dto);

    @Named("stayFromId")
    default Stay stayFromId(Long id) {
        if (id == null) {
            return null;
        }
        Stay stay = new Stay();
        stay.setId(id);
        return stay;
    }

    @Named("sessionFromId")
    default Session sessionFromId(Long id) {
        if (id == null) {
            return null;
        }
        Session session = new Session();
        session.setId(id);
        return session;
    }
}
