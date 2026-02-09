package com.example.mapper;

import com.example.dto.SeatDto;
import com.example.models.DiningTable;
import com.example.models.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    @Mapping(target = "diningTableId", source = "diningTable.id")
    SeatDto toDto(Seat entity);

    @Mapping(target = "diningTable", source = "diningTableId", qualifiedByName = "diningTableFromId")
    Seat toEntity(SeatDto dto);

    @Named("diningTableFromId")
    default DiningTable diningTableFromId(Long id) {
        if (id == null) {
            return null;
        }
        DiningTable table = new DiningTable();
        table.setId(id);
        return table;
    }
}
