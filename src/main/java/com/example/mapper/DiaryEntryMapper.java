package mapper;

import dto.DiaryEntryDto;
import model.DiaryEntry;
import model.MedicalCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DiaryEntryMapper {
    @Mapping(target = "medicalCardId", source = "medicalCard.id")
    DiaryEntryDto toDto(DiaryEntry entity);

    @Mapping(target = "medicalCard", source = "medicalCardId", qualifiedByName = "medicalCardFromId")
    DiaryEntry toEntity(DiaryEntryDto dto);

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
