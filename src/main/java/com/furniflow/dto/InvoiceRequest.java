
package com.furniflow.dto;

import java.time.LocalDate;

public record InvoiceRequest(
        LocalDate invoiceDate,
        LocalDate dueDate
) {}
