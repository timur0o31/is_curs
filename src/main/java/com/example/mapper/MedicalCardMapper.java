package mapper;

import dto.MedicalCardDto;
import model.MedicalCard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicalCardMapper {
    MedicalCardDto toDto(MedicalCard entity);

    MedicalCard toEntity(MedicalCardDto dto);
}
