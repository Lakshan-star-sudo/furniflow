package com.furniflow.service;

import com.furniflow.entity.Material;

import java.math.BigDecimal;
import java.util.List;

public interface MaterialService {

    Material createMaterial(Material material);

    List<Material> getAllMaterials();

    Material getMaterialById(Long id);

    Material updateMaterial(Long id, Material material);

    Material updateStock(Long id, BigDecimal quantity);

  

    void deleteMaterial(Long id);
}