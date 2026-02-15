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
public class PublicEventDto {
    private Long id;
    private String title;
    private LocalDate date;
    private LocalTime timeStart;
    private String description;
}
