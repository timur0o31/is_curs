package com.example.mapper;

import com.example.dto.DoctorDto;
import com.example.models.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mapping(source = "user.name", target = "name")
    DoctorDto toDto(Doctor doctor);
}
