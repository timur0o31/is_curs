package controller.stay;

import dto.stay.StayRequestCreateDto;
import dto.stay.StayRequestDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import model.RequestStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.stay.StayRequestService;

@RestController
@RequestMapping("/api/stay-requests")
@RequiredArgsConstructor
public class StayRequestController {
    private final StayRequestService service;

    @GetMapping
    public List<StayRequestDto> getAll() {
        return service.getAll();
    }

    @GetMapping(params = "patientId")
    public List<StayRequestDto> getByPatientId(@RequestParam Long patientId) {
        return service.getByPatientId(patientId);
    }

    @GetMapping(params = "status")
    public List<StayRequestDto> getByStatus(@RequestParam RequestStatus status) {
        return service.getByStatus(status);
    }

    @GetMapping("/{id}")
    public StayRequestDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public StayRequestDto create(@RequestBody StayRequestDto dto) {
        return service.create(dto);
    }

    @PostMapping("/check-in")
    public StayRequestDto createCheckIn(@RequestBody StayRequestCreateDto dto) {
        return service.createCheckInRequest(dto.getPatientId(), dto.getAdmissionDate(), dto.getDischargeDate());
    }

    @PostMapping("/expansion")
    public StayRequestDto createExpansion(@RequestBody StayRequestCreateDto dto) {
        return service.createExpansionRequest(dto.getPatientId(), dto.getAdmissionDate(), dto.getDischargeDate());
    }

    @PutMapping("/{id}")
    public StayRequestDto update(@PathVariable Long id, @RequestBody StayRequestDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/{id}/approve")
    public Long approve(@PathVariable Long id, @RequestParam Long roomId, @RequestParam(required = false) Long doctorId) {
        return service.approveRequest(id, roomId, doctorId);
    }

    @PostMapping("/{id}/reject")
    public void reject(@PathVariable Long id) {
        service.rejectRequest(id);
    }
}
