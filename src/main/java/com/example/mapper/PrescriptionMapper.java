package com.example.mapper;

import com.example.dto.PrescriptionDto;
import com.example.models.MedicalCard;
import com.example.models.Medicament;
import com.example.models.Prescription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {
    @Mapping(target = "medicamentId", source = "medicament.id")
    @Mapping(target = "medicalCardId", source = "medicalCard.id")
    PrescriptionDto toDto(Prescription entity);

    @Mapping(target = "medicament", source = "medicamentId", qualifiedByName = "medicamentFromId")
    @Mapping(target = "medicalCard", source = "medicalCardId", qualifiedByName = "medicalCardFromId")
    Prescription toEntity(PrescriptionDto dto);

    @Named("medicamentFromId")
    default Medicament medicamentFromId(Long id) {
        if (id == null) {
            return null;
        }
        Medicament medicament = new Medicament();
        medicament.setId(id);
        return medicament;
    }

    @Named("medicalCardFromId")
    default MedicalCard medicalCardFromId(Long id) {
        if (id == null) {
            return null;
        }
        MedicalCard medicalCard = new MedicalCard();
        medicalCard.setId(id);
        return medicalCard;
    }
}
