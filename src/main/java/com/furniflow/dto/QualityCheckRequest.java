
package com.furniflow.dto;

import java.time.LocalDate;

public record QualityCheckRequest(
        LocalDate checkDate,
        String result,
        String remarks
) {}
