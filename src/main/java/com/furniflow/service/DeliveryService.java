
package com.furniflow.service;

import com.furniflow.entity.Delivery;
import java.util.List;

public interface DeliveryService {

    Delivery createDelivery(Long orderId, Delivery delivery);

    List<Delivery> getAllDeliveries();

    Delivery getDeliveryById(Long deliveryId);

    List<Delivery> getDeliveriesByOrder(Long orderId);

    Delivery updateDelivery(Long deliveryId, Delivery delivery);

    void deleteDelivery(Long deliveryId);
}
