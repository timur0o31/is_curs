package com.example.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDto {
    private Long id;
    private Long medicamentId;
    private Long medicalCardId;
    private Integer dosage;
    private Integer frequency;
    private LocalDate startDate;
    private LocalDate endDate;
}
