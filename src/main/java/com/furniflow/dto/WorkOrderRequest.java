
package com.furniflow.dto;

import java.time.LocalDate;

public record WorkOrderRequest(
        LocalDate createdDate,
        LocalDate plannedCompletionDate,
        String status
) {}
