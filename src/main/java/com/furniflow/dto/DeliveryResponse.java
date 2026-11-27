
package com.furniflow.dto;

import java.time.LocalDate;

public record DeliveryResponse(
        Long deliveryId,
        Long orderId,
        LocalDate deliveryDate,
        String deliveryAddress,
        String status
) {}
