package com.example.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.models.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.models.RequestStatus;
import com.example.models.RequestType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StayRequestDto {
    private Long id;
    private Long patientId;
    private RequestStatus status;
    private LocalDateTime createdAt;
    private RequestType type;
    private LocalDate admissionDate;
    private LocalDate dischargeDate;
}
