
package com.furniflow.repository;

import com.furniflow.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findByOrder_OrderId(Long orderId);

    boolean existsByOrder_OrderId(Long orderId);
}
