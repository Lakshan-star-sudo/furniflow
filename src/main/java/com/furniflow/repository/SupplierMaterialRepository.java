package com.furniflow.repository;

import com.furniflow.entity.SupplierMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierMaterialRepository
        extends JpaRepository<SupplierMaterial, Long> {

    List<SupplierMaterial> findBySupplierSupplierId(Long supplierId);

    List<SupplierMaterial> findByMaterialMaterialId(Long materialId);
}