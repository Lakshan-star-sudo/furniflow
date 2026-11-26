
package com.furniflow.dto;

import java.time.LocalDate;

public record QualityCheckResponse(
        Long qualityCheckId,
        Long stageId,
        LocalDate checkDate,
        String result,
        String remarks
) {}
