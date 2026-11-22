package com.furniflow.service;

import com.furniflow.entity.Material;
import com.furniflow.entity.Supplier;
import com.furniflow.entity.SupplierMaterial;
import com.furniflow.exception.ResourceNotFoundException;
import com.furniflow.repository.MaterialRepository;
import com.furniflow.repository.SupplierMaterialRepository;
import com.furniflow.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SupplierMaterialServiceImpl
        implements SupplierMaterialService {

    private final SupplierMaterialRepository supplierMaterialRepository;
    private final SupplierRepository supplierRepository;
    private final MaterialRepository materialRepository;

    public SupplierMaterialServiceImpl(
            SupplierMaterialRepository supplierMaterialRepository,
            SupplierRepository supplierRepository,
            MaterialRepository materialRepository) {

        this.supplierMaterialRepository =
                supplierMaterialRepository;

        this.supplierRepository = supplierRepository;
        this.materialRepository = materialRepository;
    }

    @Override
    public SupplierMaterial createSupplierMaterial(
            SupplierMaterial supplierMaterial) {

        if (supplierMaterial.getSupplier() == null ||
                supplierMaterial.getSupplier().getSupplierId() == null) {

            throw new RuntimeException("Supplier is required");
        }

        if (supplierMaterial.getMaterial() == null ||
                supplierMaterial.getMaterial().getMaterialId() == null) {

            throw new RuntimeException("Material is required");
        }

        if (supplierMaterial.getUnitPrice() == null) {
            throw new RuntimeException("Unit price is required");
        }

        if (supplierMaterial.getUnitPrice()
                .compareTo(BigDecimal.ZERO) < 0) {

            throw new RuntimeException(
                    "Unit price cannot be negative");
        }

        Supplier supplier = supplierRepository
                .findById(
                        supplierMaterial
                                .getSupplier()
                                .getSupplierId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found"));

        Material material = materialRepository
                .findById(
                        supplierMaterial
                                .getMaterial()
                                .getMaterialId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Material not found"));

        if (supplierMaterial.getAvailabilityStatus() == null ||
                supplierMaterial.getAvailabilityStatus()
                        .trim().isEmpty()) {

            supplierMaterial.setAvailabilityStatus("AVAILABLE");
        }

        supplierMaterial.setSupplier(supplier);
        supplierMaterial.setMaterial(material);

        return supplierMaterialRepository.save(
                supplierMaterial);
    }

    @Override
    public List<SupplierMaterial> getAllSupplierMaterials() {
        return supplierMaterialRepository.findAll();
    }

    @Override
    public List<SupplierMaterial> getBySupplierId(
            Long supplierId) {

        if (!supplierRepository.existsById(supplierId)) {
            throw new ResourceNotFoundException(
                    "Supplier not found");
        }

        return supplierMaterialRepository
                .findBySupplierSupplierId(supplierId);
    }

    @Override
    public List<SupplierMaterial> getByMaterialId(
            Long materialId) {

        if (!materialRepository.existsById(materialId)) {
            throw new ResourceNotFoundException(
                    "Material not found");
        }

        return supplierMaterialRepository
                .findByMaterialMaterialId(materialId);
    }

    @Override
    public SupplierMaterial getSupplierMaterialById(
            Long id) {

        return supplierMaterialRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier material not found"));
    }

    @Override
    public SupplierMaterial updateSupplierMaterial(
            Long id,
            SupplierMaterial supplierMaterial) {

        SupplierMaterial existing =
                supplierMaterialRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Supplier material not found"));

        if (supplierMaterial.getUnitPrice() != null) {

            if (supplierMaterial.getUnitPrice()
                    .compareTo(BigDecimal.ZERO) < 0) {

                throw new RuntimeException(
                        "Unit price cannot be negative");
            }

            existing.setUnitPrice(
                    supplierMaterial.getUnitPrice());
        }

        if (supplierMaterial.getAvailabilityStatus() != null) {

            existing.setAvailabilityStatus(
                    supplierMaterial
                            .getAvailabilityStatus());
        }

        if (supplierMaterial.getSupplier() != null &&
                supplierMaterial.getSupplier()
                        .getSupplierId() != null) {

            Supplier supplier = supplierRepository
                    .findById(
                            supplierMaterial
                                    .getSupplier()
                                    .getSupplierId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Supplier not found"));

            existing.setSupplier(supplier);
        }

        if (supplierMaterial.getMaterial() != null &&
                supplierMaterial.getMaterial()
                        .getMaterialId() != null) {

            Material material = materialRepository
                    .findById(
                            supplierMaterial
                                    .getMaterial()
                                    .getMaterialId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Material not found"));

            existing.setMaterial(material);
        }

        return supplierMaterialRepository.save(existing);
    }

    @Override
    public void deleteSupplierMaterial(Long id) {

        SupplierMaterial existing =
                supplierMaterialRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Supplier material not found"));

        supplierMaterialRepository.delete(existing);
    }
}