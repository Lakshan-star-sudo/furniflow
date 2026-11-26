package com.furniflow.repository;

import com.furniflow.entity.OrderProduct;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

import com.furniflow.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductRepository
        extends JpaRepository<OrderProduct, Long> {

    List<OrderProduct> findByOrderOrderId(Long orderId);

@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT op FROM OrderProduct op WHERE op.orderProductId = :id")
Optional<OrderProduct> findByIdForUpdate(@Param("id") Long id);

}