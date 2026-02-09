package com.example.controllers;

import com.example.dto.DiaryEntryDto;
import com.example.dto.MedicalCardDto;
import com.example.dto.PrescriptionDto;
import com.example.dto.SessionDto;
import com.example.models.Diet;
import com.example.services.DiaryEntryService;
import com.example.services.MedicalCardService;
import com.example.services.PrescriptionService;
import com.example.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final MedicalCardService medicalCardService;
    private final DiaryEntryService diaryEntryService;
    private final PrescriptionService prescriptionService;
    private final SessionService sessionService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<Map<String, String>> getDoctorDashboard(Authentication authentication) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Панель врача");
        response.put("user", authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patients")
    @PreAuthorize("hasAuthority('patient:read')")
    public ResponseEntity<String> getPatients() {
        return ResponseEntity.ok("Список пациентов врача");
    }

    /**
     * UC-15: Просмотр медкарты и дневника пациента.
     * Возвращает медицинскую карту по идентификатору пациента.
     */
    @GetMapping("/medical-cards/{patientId}")
    @PreAuthorize("hasAuthority('medical_card:read')")
    public ResponseEntity<MedicalCardDto> getMedicalCard(@PathVariable Long patientId) {
        MedicalCardDto card = medicalCardService.getByPatientId(patientId);
        return ResponseEntity.ok(card);
    }

    /**
     * UC-15: Просмотр дневника пациента.
     */
    @GetMapping("/patients/{patientId}/diary")
    @PreAuthorize("hasAuthority('diary_entry:read')")
    public ResponseEntity<List<DiaryEntryDto>> getPatientDiary(@PathVariable Long patientId) {
        MedicalCardDto card = medicalCardService.getByPatientId(patientId);
        List<DiaryEntryDto> entries = diaryEntryService.getByMedicalCardId(card.getId());
        return ResponseEntity.ok(entries);
    }

    /**
     * UC-17: Назначение диеты с автоматическим учетом при рассадке.
     */
    @PostMapping("/patients/{patientId}/diet")
    @PreAuthorize("hasAuthority('medical_card:write')")
    public ResponseEntity<MedicalCardDto> updatePatientDiet(
            @PathVariable Long patientId,
            @RequestParam Diet diet) {
        MedicalCardDto updated = medicalCardService.updateDietAndAssignSeat(patientId, diet);
        return ResponseEntity.ok(updated);
    }

    /**
     * UC-19: Управление расписанием консультаций.
     * Получение свободных слотов консультаций врача (сеансы без процедуры).
     */
    @GetMapping("/consultations")
    @PreAuthorize("hasAuthority('session:read')")
    public ResponseEntity<List<SessionDto>> getConsultationSlots(@RequestParam Long doctorId) {
        List<SessionDto> allSessions = sessionService.getByDoctorId(doctorId);
        List<SessionDto> consultations = allSessions.stream()
                .filter(s -> s.getProcedureId() == null)
                .toList();
        return ResponseEntity.ok(consultations);
    }

    /**
     * UC-19: Управление расписанием консультаций.
     * Врач указывает свободные слоты, система публикует их для пациентов.
     */
    @PostMapping("/consultations")
    @PreAuthorize("hasAuthority('session:write')")
    public ResponseEntity<List<SessionDto>> createConsultationSlots(
            @RequestBody List<ConsultationSlotRequest> slots) {
        List<SessionDto> created = slots.stream()
                .map(slot -> sessionService.create(
                        new SessionDto(null, null, slot.doctorId(), slot.timeStart())))
                .toList();
        return ResponseEntity.ok(created);
    }

    /**
     * UC-18: Назначение лекарств.
     * Врач указывает препарат и график, система сохраняет назначение.
     */
    @PostMapping("/patients/{patientId}/prescriptions")
    @PreAuthorize("hasAuthority('prescription:write')")
    public ResponseEntity<PrescriptionDto> createPrescriptionForPatient(
            @PathVariable Long patientId,
            @RequestBody PrescriptionCreateRequest request) {
        MedicalCardDto card = medicalCardService.getByPatientId(patientId);
        PrescriptionDto dto = new PrescriptionDto(
                null,
                request.medicamentId(),
                card.getId(),
                request.dosage(),
                request.frequency(),
                request.startDate(),
                request.endDate()
        );
        PrescriptionDto created = prescriptionService.create(dto);
        return ResponseEntity.ok(created);
    }

    /**
     * UC-20: Комментарии пациенту (рекомендации).
     * Врач добавляет рекомендацию, пациент видит запись в дневнике.
     */
    @PostMapping("/patients/{patientId}/comments")
    @PreAuthorize("hasAuthority('diary_entry:write')")
    public ResponseEntity<DiaryEntryDto> addCommentForPatient(
            @PathVariable Long patientId,
            @RequestBody String comment) {
        MedicalCardDto card = medicalCardService.getByPatientId(patientId);
        DiaryEntryDto entry = diaryEntryService.createForMedicalCard(card.getId(), comment);
        return ResponseEntity.ok(entry);
    }

    public record ConsultationSlotRequest(Long doctorId, LocalTime timeStart) {}

    public record PrescriptionCreateRequest(
            Long medicamentId,
            Integer dosage,
            Integer frequency,
            LocalDate startDate,
            LocalDate endDate
    ) {}
}
