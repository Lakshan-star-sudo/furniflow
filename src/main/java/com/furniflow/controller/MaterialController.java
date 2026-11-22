package com.furniflow.controller;

import com.furniflow.entity.Material;
import com.furniflow.service.MaterialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping
    public ResponseEntity<Material> createMaterial(
            @RequestBody Material material) {

        return ResponseEntity.ok(
                materialService.createMaterial(material)
        );
    }

    @GetMapping
    public ResponseEntity<List<Material>> getAllMaterials() {

        return ResponseEntity.ok(
                materialService.getAllMaterials()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterialById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                materialService.getMaterialById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Material> updateMaterial(
            @PathVariable Long id,
            @RequestBody Material material) {

        return ResponseEntity.ok(
                materialService.updateMaterial(id, material)
        );
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Material> updateStock(
            @PathVariable Long id,
            @RequestParam BigDecimal quantity) {

        return ResponseEntity.ok(
                materialService.updateStock(id, quantity)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(
            @PathVariable Long id) {

        materialService.deleteMaterial(id);

        return ResponseEntity.noContent().build();
    }
}