package com.example.mapper;

import com.example.dto.StayRequestDto;
import com.example.models.Patient;
import com.example.models.StayRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface StayRequestMapper {

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.user.name", target = "patientName")
    StayRequestDto toDto(StayRequest entity);

    @Mapping(source = "patientId", target = "patient", qualifiedByName = "patientIdToPatient")
    StayRequest toEntity(StayRequestDto dto);

    @Named("patientIdToPatient")
    default Patient patientIdToPatient(Long patientId) {
        if (patientId == null) {
            return null;
        }
        Patient patient = new Patient();
        patient.setId(patientId);
        return patient;
    }
}
