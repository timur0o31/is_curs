package controller;

import dto.RoomDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.RoomService;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService service;

    @GetMapping
    public List<RoomDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/available")
    public List<RoomDto> getAvailable() {
        return service.getAvailableRooms();
    }

    @GetMapping("/{id}")
    public RoomDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public RoomDto create(@RequestBody RoomDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public RoomDto update(@PathVariable Long id, @RequestBody RoomDto dto) {
        return service.update(id, dto);
    }

    @PutMapping("/{id}/occupancy")
    public RoomDto updateOccupancy(@PathVariable Long id, @RequestParam Boolean isOccupied) {
        return service.updateOccupancy(id, isOccupied);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
