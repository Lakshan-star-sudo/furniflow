
package com.furniflow.dto;

import java.time.LocalDate;

public record ProductionStageRequest(
        String stageName,
        LocalDate startDate,
        LocalDate endDate,
        String status,
        String remarks
) {}
