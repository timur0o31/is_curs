package com.example.dto;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcedureDto {
    private Long id;
    private String name;
    private String description;
    private Integer defaultSeats;
    private Duration duration;
}
