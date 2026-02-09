package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StayDto {
    private Long id;
    private Long stayRequestId;
    private Long roomId;
    private Long doctorId;
}
