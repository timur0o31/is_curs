package com.example.mapper;

import com.example.dto.DoctorDto;
import com.example.models.Doctor;
import com.example.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mapping(source = "user.name", target = "name")
    DoctorDto toDto(Doctor doctor);

    @Mapping(target = "user", expression = "java(userFromName(dto.getName()))")
    Doctor toEntity(DoctorDto dto);

    default User userFromName(String name) {
        if (name == null) {
            return null;
        }
        User user = new User();
        user.setName(name);
        return user;
    }
}
