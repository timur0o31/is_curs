package com.example.mapper;

import com.example.dto.NotificationDto;
import com.example.models.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "user.id", target = "userId")
    NotificationDto toDto(Notification entity);

    @Mapping(source = "userId", target = "user.id")
    Notification toEntity(NotificationDto dto);
}

