
package com.furniflow.dto;

import java.time.LocalDate;

public record DeliveryRequest(
        LocalDate deliveryDate,
        String deliveryAddress,
        String status
) {}
