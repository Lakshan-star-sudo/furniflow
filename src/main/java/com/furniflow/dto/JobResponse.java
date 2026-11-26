
package com.furniflow.dto;

import java.time.LocalDate;

public record JobResponse(
        Long jobId,
        Long workOrderItemId,
        String jobName,
        LocalDate startDate,
        LocalDate endDate,
        String status
) {}
