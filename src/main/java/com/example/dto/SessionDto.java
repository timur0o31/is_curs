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
public class SessionDto {
    private Long id;
    private Long procedureId;
    private Long doctorId;
    private LocalDate sessionDate;
    private LocalTime timeStart;
}
