package com.example.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientScheduleItemDto {
    private Long sessionId;
    private LocalDate sessionDate;
    private LocalTime timeStart;
    private Long procedureId;
    private String procedureName;
    private Boolean isNecessary;
}
