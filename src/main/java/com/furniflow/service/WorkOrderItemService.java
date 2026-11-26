
package com.furniflow.service;

import com.furniflow.entity.WorkOrderItem;
import java.util.List;

public interface WorkOrderItemService {

    WorkOrderItem createWorkOrderItem(
            Long workOrderId,
            Long orderProductId,
            Integer quantity);

    List<WorkOrderItem> getAllWorkOrderItems();

    WorkOrderItem getWorkOrderItemById(Long workOrderItemId);

    List<WorkOrderItem> getItemsByWorkOrder(Long workOrderId);

    WorkOrderItem updateWorkOrderItem(
            Long workOrderItemId,
            Integer quantity,
            String status);

    void deleteWorkOrderItem(Long workOrderItemId);
}
