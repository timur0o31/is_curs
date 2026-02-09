package dto;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentDto {
    private Long id;
    private String name;
    private String description;
    private LocalTime[] intakeTime;
}
