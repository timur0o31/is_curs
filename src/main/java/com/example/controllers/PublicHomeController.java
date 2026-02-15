package com.example.controllers;

import com.example.dto.PublicDoctorDto;
import com.example.dto.PublicEventDto;
import com.example.dto.PublicHomeDto;
import com.example.dto.PublicProcedureDto;
import com.example.services.PublicHomeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicHomeController {

    private final PublicHomeService publicHomeService;

    @GetMapping("/procedures")
    public ResponseEntity<List<PublicProcedureDto>> getProcedures() {
        return ResponseEntity.ok(publicHomeService.getProcedures());
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<PublicDoctorDto>> getDoctors() {
        return ResponseEntity.ok(publicHomeService.getDoctors());
    }

    @GetMapping("/events")
    public ResponseEntity<List<PublicEventDto>> getEvents() {
        return ResponseEntity.ok(publicHomeService.getEvents());
    }

    @GetMapping("/home")
    public ResponseEntity<PublicHomeDto> getHome() {
        return ResponseEntity.ok(publicHomeService.getHome());
    }
}
