package com.example.dto;

import com.example.models.RequestStatus;
import com.example.models.RequestType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StayRequestDto {
    private Long id;

    private Long patientId;
    private String patientName;

    private RequestStatus status;
    private RequestType type;

    private LocalDate admissionDate;
    private LocalDate dischargeDate;

    private LocalDateTime createdAt;
}
