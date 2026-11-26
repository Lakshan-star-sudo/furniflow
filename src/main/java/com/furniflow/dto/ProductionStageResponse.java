
package com.furniflow.dto;

import java.time.LocalDate;

public record ProductionStageResponse(
        Long stageId,
        Long jobId,
        String stageName,
        LocalDate startDate,
        LocalDate endDate,
        String status,
        String remarks
) {}
