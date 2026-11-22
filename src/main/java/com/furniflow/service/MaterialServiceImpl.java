package com.furniflow.service;

import com.furniflow.entity.Material;
import com.furniflow.entity.Warehouse;
import com.furniflow.exception.ResourceNotFoundException;
import com.furniflow.repository.MaterialRepository;
import com.furniflow.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final WarehouseRepository warehouseRepository;

    public MaterialServiceImpl(
            MaterialRepository materialRepository,
            WarehouseRepository warehouseRepository) {

        this.materialRepository = materialRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public Material createMaterial(Material material) {

        if (material.getMaterialName() == null ||
                material.getMaterialName().trim().isEmpty()) {
            throw new RuntimeException("Material name is required");
        }

        if (material.getUnit() == null ||
                material.getUnit().trim().isEmpty()) {
            throw new RuntimeException("Material unit is required");
        }

        if (material.getWarehouse() == null ||
                material.getWarehouse().getWarehouseId() == null) {
            throw new RuntimeException("Warehouse is required");
        }

        Warehouse warehouse = warehouseRepository
                .findById(material.getWarehouse().getWarehouseId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Warehouse not found"));

        if (material.getQuantityInStock() == null) {
            material.setQuantityInStock(BigDecimal.ZERO);
        }

        if (material.getMinimumStockLevel() == null) {
            material.setMinimumStockLevel(BigDecimal.ZERO);
        }

        if (material.getUnitCost() == null) {
            material.setUnitCost(BigDecimal.ZERO);
        }

        validateNonNegative(
                material.getQuantityInStock(),
                "Stock quantity");

        validateNonNegative(
                material.getMinimumStockLevel(),
                "Minimum stock level");

        validateNonNegative(
                material.getUnitCost(),
                "Unit cost");

        if (material.getStatus() == null ||
                material.getStatus().trim().isEmpty()) {
            material.setStatus("ACTIVE");
        }

        material.setWarehouse(warehouse);

        return materialRepository.save(material);
    }

    @Override
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    @Override
    public Material getMaterialById(Long id) {

        return materialRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Material not found"));
    }

    @Override
    public Material updateMaterial(
            Long id,
            Material material) {

        Material existing = materialRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Material not found"));

        if (material.getMaterialName() != null) {
            existing.setMaterialName(
                    material.getMaterialName());
        }

        if (material.getCategory() != null) {
            existing.setCategory(
                    material.getCategory());
        }

        if (material.getUnit() != null) {
            existing.setUnit(material.getUnit());
        }

        if (material.getMinimumStockLevel() != null) {

            validateNonNegative(
                    material.getMinimumStockLevel(),
                    "Minimum stock level");

            existing.setMinimumStockLevel(
                    material.getMinimumStockLevel());
        }

        if (material.getUnitCost() != null) {

            validateNonNegative(
                    material.getUnitCost(),
                    "Unit cost");

            existing.setUnitCost(
                    material.getUnitCost());
        }

        if (material.getStatus() != null) {
            existing.setStatus(material.getStatus());
        }

        if (material.getWarehouse() != null &&
                material.getWarehouse().getWarehouseId() != null) {

            Warehouse warehouse = warehouseRepository
                    .findById(
                            material.getWarehouse().getWarehouseId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Warehouse not found"));

            existing.setWarehouse(warehouse);
        }

        return materialRepository.save(existing);
    }

    @Override
    public Material updateStock(
            Long id,
            BigDecimal quantity) {

        Material material = getMaterialById(id);

        if (quantity == null) {
            throw new RuntimeException(
                    "Stock quantity is required");
        }

        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException(
                    "Stock quantity cannot be negative");
        }

        material.setQuantityInStock(quantity);

        return materialRepository.save(material);
    }

    

    @Override
    public void deleteMaterial(Long id) {

        Material material = materialRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Material not found"));

        materialRepository.delete(material);
    }

    private void validateNonNegative(
            BigDecimal value,
            String fieldName) {

        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException(
                    fieldName + " cannot be negative");
        }
    }
}