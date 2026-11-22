package com.furniflow.repository;

import com.furniflow.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductRepository
        extends JpaRepository<OrderProduct, Long> {

    List<OrderProduct> findByOrderOrderId(Long orderId);
}