
package com.furniflow.controller;

import com.furniflow.dto.DeliveryRequest;
import com.furniflow.dto.DeliveryResponse;
import com.furniflow.entity.Delivery;
import com.furniflow.service.DeliveryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    private Delivery toEntity(DeliveryRequest request) {
        Delivery delivery = new Delivery();
        delivery.setDeliveryDate(request.deliveryDate());
        delivery.setDeliveryAddress(request.deliveryAddress());
        delivery.setStatus(request.status());
        return delivery;
    }

    private DeliveryResponse toResponse(Delivery delivery) {
        return new DeliveryResponse(
                delivery.getDeliveryId(),
                delivery.getOrder().getOrderId(),
                delivery.getDeliveryDate(),
                delivery.getDeliveryAddress(),
                delivery.getStatus()
        );
    }

    @PostMapping
    public ResponseEntity<DeliveryResponse> create(
            @RequestParam Long orderId,
            @RequestBody DeliveryRequest request) {

        Delivery saved = deliveryService.createDelivery(
                orderId, toEntity(request));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<DeliveryResponse>> getAll() {
        return ResponseEntity.ok(
                deliveryService.getAllDeliveries()
                        .stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                toResponse(deliveryService.getDeliveryById(id))
        );
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<DeliveryResponse>> getByOrder(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                deliveryService.getDeliveriesByOrder(orderId)
                        .stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryResponse> update(
            @PathVariable Long id,
            @RequestBody DeliveryRequest request) {

        return ResponseEntity.ok(
                toResponse(deliveryService.updateDelivery(
                        id, toEntity(request)))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        deliveryService.deleteDelivery(id);
        return ResponseEntity.noContent().build();
    }
}
