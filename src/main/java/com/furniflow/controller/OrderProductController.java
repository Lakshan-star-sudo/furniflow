package com.furniflow.controller;

import com.furniflow.entity.OrderProduct;
import com.furniflow.service.OrderProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-products")
public class OrderProductController {

    private final OrderProductService orderProductService;

    public OrderProductController(
            OrderProductService orderProductService) {

        this.orderProductService = orderProductService;
    }

    @PostMapping
    public ResponseEntity<OrderProduct> addOrderProduct(
            @RequestBody OrderProduct orderProduct) {

        return ResponseEntity.ok(
                orderProductService.addOrderProduct(orderProduct)
        );
    }

    @GetMapping
    public ResponseEntity<List<OrderProduct>> getAllOrderProducts() {

        return ResponseEntity.ok(
                orderProductService.getAllOrderProducts()
        );
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderProduct>>
    getOrderProductsByOrderId(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                orderProductService
                        .getOrderProductsByOrderId(orderId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderProduct> getOrderProductById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                orderProductService.getOrderProductById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderProduct> updateOrderProduct(
            @PathVariable Long id,
            @RequestBody OrderProduct orderProduct) {

        return ResponseEntity.ok(
                orderProductService
                        .updateOrderProduct(id, orderProduct)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderProduct(
            @PathVariable Long id) {

        orderProductService.deleteOrderProduct(id);

        return ResponseEntity.noContent().build();
    }
}