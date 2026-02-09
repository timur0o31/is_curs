package com.example.dto.stay;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StayRequestCreateDto {
    private Long patientId;
    private LocalDate admissionDate;
    private LocalDate dischargeDate;
}
