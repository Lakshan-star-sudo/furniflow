package com.furniflow.controller;

import com.furniflow.entity.Showroom;
import com.furniflow.service.ShowroomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/showrooms")
public class ShowroomController {

    private final ShowroomService showroomService;

    public ShowroomController(ShowroomService showroomService) {
        this.showroomService = showroomService;
    }

    @PostMapping
    public ResponseEntity<Showroom> createShowroom(
            @RequestBody Showroom showroom) {

        return ResponseEntity.ok(
                showroomService.createShowroom(showroom)
        );
    }

    @GetMapping
    public ResponseEntity<List<Showroom>> getAllShowrooms() {

        return ResponseEntity.ok(
                showroomService.getAllShowrooms()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Showroom> getShowroomById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                showroomService.getShowroomById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Showroom> updateShowroom(
            @PathVariable Long id,
            @RequestBody Showroom showroom) {

        return ResponseEntity.ok(
                showroomService.updateShowroom(id, showroom)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowroom(
            @PathVariable Long id) {

        showroomService.deleteShowroom(id);

        return ResponseEntity.noContent().build();
    }
}