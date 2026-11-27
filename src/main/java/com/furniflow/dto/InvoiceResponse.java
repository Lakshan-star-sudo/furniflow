
package com.furniflow.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvoiceResponse(
        Long invoiceId,
        Long orderId,
        LocalDate invoiceDate,
        LocalDate dueDate,
        BigDecimal totalAmount,
        String status
) {}
