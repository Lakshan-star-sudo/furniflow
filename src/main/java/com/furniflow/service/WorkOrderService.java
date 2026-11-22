package com.furniflow.service;

import com.furniflow.entity.WorkOrder;
import java.util.List;

public interface WorkOrderService {

    WorkOrder createWorkOrder(WorkOrder workOrder, Long orderId);

    List<WorkOrder> getAllWorkOrders();

    WorkOrder getWorkOrderById(Long workOrderId);

    WorkOrder updateWorkOrder(Long workOrderId, WorkOrder workOrder);

    void deleteWorkOrder(Long workOrderId);
}