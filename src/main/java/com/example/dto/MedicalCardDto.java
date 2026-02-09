package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.models.Diet;

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
