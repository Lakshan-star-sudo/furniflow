package com.furniflow.service;

import com.furniflow.entity.Warehouse;
import com.furniflow.exception.ResourceNotFoundException;
import com.furniflow.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public Warehouse createWarehouse(Warehouse warehouse) {

        if (warehouse.getWarehouseName() == null ||
                warehouse.getWarehouseName().trim().isEmpty()) {
            throw new RuntimeException("Warehouse name is required");
        }

        if (warehouse.getStatus() == null ||
                warehouse.getStatus().trim().isEmpty()) {
            warehouse.setStatus("ACTIVE");
        }

        return warehouseRepository.save(warehouse);
    }

    @Override
    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    @Override
    public Warehouse getWarehouseById(Long id) {

        return warehouseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Warehouse not found"));
    }

    @Override
    public Warehouse updateWarehouse(
            Long id,
            Warehouse warehouse) {

        Warehouse existing = warehouseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Warehouse not found"));

        if (warehouse.getWarehouseName() != null) {
            existing.setWarehouseName(
                    warehouse.getWarehouseName());
        }

        if (warehouse.getLocation() != null) {
            existing.setLocation(
                    warehouse.getLocation());
        }

        if (warehouse.getStatus() != null) {
            existing.setStatus(
                    warehouse.getStatus());
        }

        return warehouseRepository.save(existing);
    }

    @Override
    public void deleteWarehouse(Long id) {

        Warehouse existing = warehouseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Warehouse not found"));

        warehouseRepository.delete(existing);
    }
}