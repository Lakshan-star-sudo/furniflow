package com.furniflow.controller;

import com.furniflow.entity.SupplierMaterial;
import com.furniflow.service.SupplierMaterialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier-materials")
public class SupplierMaterialController {

    private final SupplierMaterialService supplierMaterialService;

    public SupplierMaterialController(
            SupplierMaterialService supplierMaterialService) {

        this.supplierMaterialService = supplierMaterialService;
    }

    @PostMapping
    public ResponseEntity<SupplierMaterial> create(
            @RequestBody SupplierMaterial supplierMaterial) {

        return ResponseEntity.ok(
                supplierMaterialService
                        .createSupplierMaterial(supplierMaterial)
        );
    }

    @GetMapping
    public ResponseEntity<List<SupplierMaterial>> getAll() {

        return ResponseEntity.ok(
                supplierMaterialService
                        .getAllSupplierMaterials()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierMaterial> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                supplierMaterialService
                        .getSupplierMaterialById(id)
        );
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<SupplierMaterial>> getBySupplier(
            @PathVariable Long supplierId) {

        return ResponseEntity.ok(
                supplierMaterialService
                        .getBySupplierId(supplierId)
        );
    }

    @GetMapping("/material/{materialId}")
    public ResponseEntity<List<SupplierMaterial>> getByMaterial(
            @PathVariable Long materialId) {

        return ResponseEntity.ok(
                supplierMaterialService
                        .getByMaterialId(materialId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierMaterial> update(
            @PathVariable Long id,
            @RequestBody SupplierMaterial supplierMaterial) {

        return ResponseEntity.ok(
                supplierMaterialService
                        .updateSupplierMaterial(
                                id,
                                supplierMaterial)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        supplierMaterialService
                .deleteSupplierMaterial(id);

        return ResponseEntity.noContent().build();
    }
}