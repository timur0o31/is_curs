package com.example.controllers;

import com.example.dto.*;
import com.example.models.Diet;
import com.example.models.Doctor;
import com.example.security.UserDetailsImpl;
import com.example.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
@PreAuthorize("@doctorAccess.canWork(authentication)")
public class DoctorController {

    private final MedicalCardService medicalCardService;
    private final DiaryEntryService diaryEntryService;
    private final PrescriptionService prescriptionService;
    private final SessionService sessionService;
    private final DoctorService doctorService;
    private final StayService stayService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<Boolean> getDoctorDashboard(@AuthenticationPrincipal UserDetailsImpl user) {
        return ResponseEntity.ok(doctorService.getWorking(user.getId()));
    }

    @GetMapping("/patients")
    @PreAuthorize("hasAuthority('patient:read')")
    public ResponseEntity<String> getPatients() {
        return ResponseEntity.ok("Список пациентов врача");
    }

    @GetMapping("/active-patients")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<ActivePatientDto>> getActivePatients(@AuthenticationPrincipal UserDetailsImpl user) {
        return ResponseEntity.ok(stayService.getPatientsByDoctorId(user.getId()));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDoctor(@RequestParam Long doctorId) {
        doctorService.deleteDoctor(doctorId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> aproveDoctor(@RequestParam Long doctorId) {
        doctorService.setWorking(doctorId, true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DoctorDto>> getAllWorkingDoctors() {
        return ResponseEntity.ok(doctorService.getAllWorkingDoctors());
    }

    @GetMapping("/no-working")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DoctorDto>> getAllNoWorkingDoctors() {
        return ResponseEntity.ok(doctorService.getAllNoWorkingDoctors());
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
                        new SessionDto(null, null, null, slot.doctorId(), slot.sessionDate(), slot.timeStart())))
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

    public record ConsultationSlotRequest(Long doctorId, LocalDate sessionDate, LocalTime timeStart) {}

    public record PrescriptionCreateRequest(
            Long medicamentId,
            Integer dosage,
            Integer frequency,
            LocalDate startDate,
            LocalDate endDate
    ) {}
}
