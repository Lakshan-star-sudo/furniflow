
package com.furniflow.dto;

public record WorkOrderItemResponse(
        Long workOrderItemId,
        Long workOrderId,
        Long orderProductId,
        Integer quantity,
        String status
) {}
