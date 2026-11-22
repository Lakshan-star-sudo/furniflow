package com.furniflow.dto;

import java.time.LocalDate;

public record WorkOrderResponse(
        Long workOrderId,
        Long orderId,
        LocalDate createdDate,
        LocalDate plannedCompletionDate,
        String status
) {}