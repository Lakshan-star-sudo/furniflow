
package com.furniflow.dto;

import java.time.LocalDate;

public record JobRequest(
        String jobName,
        LocalDate startDate,
        LocalDate endDate,
        String status
) {}
