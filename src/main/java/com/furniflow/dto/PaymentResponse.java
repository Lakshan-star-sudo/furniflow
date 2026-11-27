
package com.furniflow.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentResponse(
        Long paymentId,
        Long invoiceId,
        LocalDate paymentDate,
        BigDecimal amount,
        String paymentMethod
) {}
