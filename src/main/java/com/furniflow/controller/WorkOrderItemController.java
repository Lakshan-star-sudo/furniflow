
package com.furniflow.controller;

import com.furniflow.dto.WorkOrderItemRequest;
import com.furniflow.dto.WorkOrderItemResponse;
import com.furniflow.entity.WorkOrderItem;
import com.furniflow.service.WorkOrderItemService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/work-order-items")
public class WorkOrderItemController {

    private final WorkOrderItemService service;

    public WorkOrderItemController(WorkOrderItemService service) {
        this.service = service;
    }

    private WorkOrderItemResponse toResponse(WorkOrderItem item) {
        return new WorkOrderItemResponse(
                item.getWorkOrderItemId(),
                item.getWorkOrder().getWorkOrderId(),
                item.getOrderProduct().getOrderProductId(),
                item.getQuantity(),
                item.getStatus()
        );
    }

    @PostMapping
    public ResponseEntity<WorkOrderItemResponse> create(
            @RequestParam Long workOrderId,
            @RequestParam Long orderProductId,
            @RequestBody WorkOrderItemRequest request) {

        WorkOrderItem saved = service.createWorkOrderItem(
                workOrderId,
                orderProductId,
                request.quantity()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<WorkOrderItemResponse>> getAll() {

        List<WorkOrderItemResponse> responses = service
                .getAllWorkOrderItems()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkOrderItemResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                toResponse(service.getWorkOrderItemById(id))
        );
    }

    @GetMapping("/work-order/{workOrderId}")
    public ResponseEntity<List<WorkOrderItemResponse>> getByWorkOrder(
            @PathVariable Long workOrderId) {

        List<WorkOrderItemResponse> responses = service
                .getItemsByWorkOrder(workOrderId)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkOrderItemResponse> update(
            @PathVariable Long id,
            @RequestBody WorkOrderItemRequest request) {

        WorkOrderItem updated = service.updateWorkOrderItem(
                id,
                request.quantity(),
                request.status()
        );

        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        service.deleteWorkOrderItem(id);
        return ResponseEntity.noContent().build();
    }
}
