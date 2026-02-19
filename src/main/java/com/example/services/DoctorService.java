package com.example.services;

import com.example.dto.DoctorDto;
import com.example.mapper.DoctorMapper;
import com.example.models.Doctor;
import com.example.repositories.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper mapper;

    public List<DoctorDto> getAllWorkingDoctors() {
        return doctorRepository.findByWorkingTrue()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<DoctorDto> getAllNoWorkingDoctors() {
        return doctorRepository.findByWorkingFalse()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    @Transactional
    public Doctor setWorking(Long doctorId, boolean working) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + doctorId));
        doctor.setWorking(working);
        return doctorRepository.save(doctor);
    }

    public boolean getWorking(Long userId) {
        Doctor doctor = doctorRepository.findByUser_Id(userId)
                .orElseThrow(()->new IllegalArgumentException("User Not Found"));
        return doctor.isWorking();
    }
}
