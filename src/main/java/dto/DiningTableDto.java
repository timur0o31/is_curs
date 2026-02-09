package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.Diet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiningTableDto {
    private Long id;
    private Integer tableNumber;
    private Diet diet;
}
