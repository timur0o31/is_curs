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
public class MedicalCardDto {
    private Long id;
    private Long patientId;
    private Diet diet;
    private String phoneNumber;
    private Integer height;
    private Integer weight;
}
