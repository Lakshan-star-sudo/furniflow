
package com.furniflow.service;

import com.furniflow.entity.Delivery;
import com.furniflow.entity.Order;
import com.furniflow.exception.ResourceNotFoundException;
import com.furniflow.repository.DeliveryRepository;
import com.furniflow.repository.OrderRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;

    private static final Set<String> VALID_STATUSES =
            Set.of("PENDING", "IN_TRANSIT", "DELIVERED", "CANCELLED");

    public DeliveryServiceImpl(
            DeliveryRepository deliveryRepository,
            OrderRepository orderRepository) {

        this.deliveryRepository = deliveryRepository;
        this.orderRepository = orderRepository;
    }

    private Delivery findDelivery(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Delivery not found with ID: " + deliveryId));
    }

    private void validateAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException(
                    "Delivery address is required");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException(
                    "Delivery date is required");
        }

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Delivery date cannot be in the past");
        }
    }

    private void validateStatus(String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new IllegalArgumentException(
                    "Invalid delivery status");
        }
    }

    private void validateTransition(
            String currentStatus, String newStatus) {

        if (currentStatus.equals(newStatus)) {
            return;
        }

        boolean allowed = switch (currentStatus) {
            case "PENDING" ->
                    newStatus.equals("IN_TRANSIT")
                    || newStatus.equals("CANCELLED");

            case "IN_TRANSIT" ->
                    newStatus.equals("DELIVERED")
                    || newStatus.equals("CANCELLED");

            default -> false;
        };

        if (!allowed) {
            throw new IllegalArgumentException(
                    "Invalid delivery status transition");
        }
    }

    @Override
    @Transactional
    public Delivery createDelivery(
            Long orderId, Delivery delivery) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with ID: " + orderId));

        validateDate(delivery.getDeliveryDate());
        validateAddress(delivery.getDeliveryAddress());

        Delivery newDelivery = new Delivery();
        newDelivery.setOrder(order);
        newDelivery.setDeliveryDate(delivery.getDeliveryDate());
        newDelivery.setDeliveryAddress(
                delivery.getDeliveryAddress().trim());
        newDelivery.setStatus("PENDING");

        return deliveryRepository.save(newDelivery);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Delivery getDeliveryById(Long deliveryId) {
        return findDelivery(deliveryId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> getDeliveriesByOrder(Long orderId) {

        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException(
                    "Order not found with ID: " + orderId);
        }

        return deliveryRepository.findByOrder_OrderId(orderId);
    }

    @Override
    @Transactional
    public Delivery updateDelivery(
            Long deliveryId, Delivery updatedDelivery) {

        Delivery existing = findDelivery(deliveryId);

        if ("DELIVERED".equals(existing.getStatus())
                || "CANCELLED".equals(existing.getStatus())) {
            throw new IllegalArgumentException(
                    "Completed or cancelled deliveries cannot be updated");
        }

        if (updatedDelivery.getDeliveryDate() != null) {
            validateDate(updatedDelivery.getDeliveryDate());
            existing.setDeliveryDate(
                    updatedDelivery.getDeliveryDate());
        }

        if (updatedDelivery.getDeliveryAddress() != null) {
            validateAddress(updatedDelivery.getDeliveryAddress());
            existing.setDeliveryAddress(
                    updatedDelivery.getDeliveryAddress().trim());
        }

        if (updatedDelivery.getStatus() != null) {
            validateStatus(updatedDelivery.getStatus());
            validateTransition(
                    existing.getStatus(),
                    updatedDelivery.getStatus());

            existing.setStatus(updatedDelivery.getStatus());
        }

        return deliveryRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteDelivery(Long deliveryId) {

        Delivery delivery = findDelivery(deliveryId);

        if (!"PENDING".equals(delivery.getStatus())) {
            throw new IllegalArgumentException(
                    "Only pending deliveries can be deleted");
        }

        deliveryRepository.delete(delivery);
    }
}
