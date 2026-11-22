
package com.furniflow.controller;

import com.furniflow.dto.WorkOrderRequest;
import com.furniflow.dto.WorkOrderResponse;
import com.furniflow.entity.WorkOrder;
import com.furniflow.service.WorkOrderService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    public WorkOrderController(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    private WorkOrderResponse toResponse(WorkOrder workOrder) {
        return new WorkOrderResponse(
                workOrder.getWorkOrderId(),
                workOrder.getOrder().getOrderId(),
                workOrder.getCreatedDate(),
                workOrder.getPlannedCompletionDate(),
                workOrder.getStatus()
        );
    }

    private WorkOrder toEntity(WorkOrderRequest request) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setCreatedDate(request.createdDate());
        workOrder.setPlannedCompletionDate(
                request.plannedCompletionDate());
        workOrder.setStatus(request.status());
        return workOrder;
    }

    @PostMapping
    public ResponseEntity<WorkOrderResponse> createWorkOrder(
            @RequestParam Long orderId,
            @RequestBody WorkOrderRequest request) {

        WorkOrder saved = workOrderService.createWorkOrder(
                toEntity(request), orderId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<WorkOrderResponse>> getAllWorkOrders() {

        List<WorkOrderResponse> responses = workOrderService
                .getAllWorkOrders()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkOrderResponse> getWorkOrderById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                toResponse(workOrderService.getWorkOrderById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkOrderResponse> updateWorkOrder(
            @PathVariable Long id,
            @RequestBody WorkOrderRequest request) {

        WorkOrder updated = workOrderService.updateWorkOrder(
                id, toEntity(request));

        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkOrder(
            @PathVariable Long id) {

        workOrderService.deleteWorkOrder(id);
        return ResponseEntity.noContent().build();
    }
}
