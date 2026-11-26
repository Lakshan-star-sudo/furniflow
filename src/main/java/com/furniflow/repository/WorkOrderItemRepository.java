package com.furniflow.repository;

import com.furniflow.entity.WorkOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkOrderItemRepository
        extends JpaRepository<WorkOrderItem, Long> {

    List<WorkOrderItem> findByWorkOrder_WorkOrderId(Long workOrderId);

    List<WorkOrderItem> findByOrderProduct_OrderProductId(Long orderProductId);
}