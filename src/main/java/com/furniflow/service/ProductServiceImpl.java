package com.furniflow.service;

import com.furniflow.entity.Product;
import com.furniflow.exception.ResourceNotFoundException;
import com.furniflow.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(Product product) {

        if (product.getStockQuantity() == null) {
            product.setStockQuantity(0);
        }

        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));
    }

    @Override
    public Product updateProduct(Long id, Product product) {

        Product existing = getProductById(id);

        existing.setProductName(product.getProductName());
        existing.setCategory(product.getCategory());
        existing.setDescription(product.getDescription());
        existing.setStandardPrice(product.getStandardPrice());
        existing.setImage(product.getImage());

        // Stock is deliberately NOT updated here.
        // Stock will later be controlled by inventory operations.

        return productRepository.save(existing);
    }

    @Override
    public void deleteProduct(Long id) {

        Product existing = getProductById(id);

        productRepository.delete(existing);
    }
}