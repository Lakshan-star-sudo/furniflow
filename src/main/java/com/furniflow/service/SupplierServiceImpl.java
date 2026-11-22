package com.furniflow.service;

import com.furniflow.entity.Supplier;
import com.furniflow.exception.ResourceNotFoundException;
import com.furniflow.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Supplier createSupplier(Supplier supplier) {

        if (supplier.getSupplierName() == null ||
                supplier.getSupplierName().trim().isEmpty()) {
            throw new RuntimeException("Supplier name is required");
        }

        return supplierRepository.save(supplier);
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    public Supplier getSupplierById(Long id) {

        return supplierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found"));
    }

    @Override
    public Supplier updateSupplier(
            Long id,
            Supplier supplier) {

        Supplier existing = supplierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found"));

        if (supplier.getSupplierName() != null) {
            existing.setSupplierName(
                    supplier.getSupplierName());
        }

        if (supplier.getContactPerson() != null) {
            existing.setContactPerson(
                    supplier.getContactPerson());
        }

        if (supplier.getPhone() != null) {
            existing.setPhone(supplier.getPhone());
        }

        if (supplier.getEmail() != null) {
            existing.setEmail(supplier.getEmail());
        }

        if (supplier.getAddress() != null) {
            existing.setAddress(supplier.getAddress());
        }

        return supplierRepository.save(existing);
    }

    @Override
    public void deleteSupplier(Long id) {

        Supplier existing = supplierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found"));

        supplierRepository.delete(existing);
    }
}