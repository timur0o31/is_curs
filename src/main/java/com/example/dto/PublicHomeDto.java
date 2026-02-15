package com.example.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicHomeDto {
    private List<PublicProcedureDto> procedures;
    private List<PublicDoctorDto> doctors;
    private List<PublicEventDto> events;
}
