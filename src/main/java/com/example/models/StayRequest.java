package com.example.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Stay_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StayRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private RequestType type;

    @Column(name = "admission_date", nullable = false)
    private LocalDate admissionDate;

    @Column(name = "discharge_date", nullable = false)
    private LocalDate dischargeDate;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
