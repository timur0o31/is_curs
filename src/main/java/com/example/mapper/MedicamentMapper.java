package mapper;

import dto.MedicamentDto;
import model.Medicament;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicamentMapper {
    MedicamentDto toDto(Medicament entity);

    Medicament toEntity(MedicamentDto dto);
}
