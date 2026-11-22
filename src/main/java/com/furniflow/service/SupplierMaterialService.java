package com.furniflow.service;

import com.furniflow.entity.SupplierMaterial;

import java.util.List;

public interface SupplierMaterialService {

    SupplierMaterial createSupplierMaterial(
            SupplierMaterial supplierMaterial);

    List<SupplierMaterial> getAllSupplierMaterials();

    List<SupplierMaterial> getBySupplierId(Long supplierId);

    List<SupplierMaterial> getByMaterialId(Long materialId);

    SupplierMaterial getSupplierMaterialById(Long id);

    SupplierMaterial updateSupplierMaterial(
            Long id,
            SupplierMaterial supplierMaterial);

    void deleteSupplierMaterial(Long id);
}