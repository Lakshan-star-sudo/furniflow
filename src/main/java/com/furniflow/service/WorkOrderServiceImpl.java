
package com.furniflow.service;

import com.furniflow.entity.Order;
import com.furniflow.entity.WorkOrder;
import com.furniflow.exception.ResourceNotFoundException;
import com.furniflow.repository.OrderRepository;
import com.furniflow.repository.WorkOrderRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final OrderRepository orderRepository;

    public WorkOrderServiceImpl(
            WorkOrderRepository workOrderRepository,
            OrderRepository orderRepository) {
        this.workOrderRepository = workOrderRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public WorkOrder createWorkOrder(WorkOrder workOrder, Long orderId) {

        if (orderId == null) {
            throw new IllegalArgumentException("Order ID is required");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with ID: " + orderId));

        LocalDate today = LocalDate.now();

        if (workOrder.getCreatedDate() == null) {
            workOrder.setCreatedDate(today);
        }

        if (workOrder.getPlannedCompletionDate() == null) {
            throw new IllegalArgumentException(
                    "Planned completion date is required");
        }

        if (workOrder.getPlannedCompletionDate()
                .isBefore(workOrder.getCreatedDate())) {
            throw new IllegalArgumentException(
                    "Planned completion date cannot be before created date");
        }

        if (workOrder.getStatus() == null ||
                workOrder.getStatus().isBlank()) {
            workOrder.setStatus("PENDING");
        }

        workOrder.setWorkOrderId(null);
        workOrder.setOrder(order);

        return workOrderRepository.save(workOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getAllWorkOrders() {
        return workOrderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public WorkOrder getWorkOrderById(Long workOrderId) {
        return workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Work order not found with ID: " + workOrderId));
    }

    @Override
    @Transactional
    public WorkOrder updateWorkOrder(
            Long workOrderId, WorkOrder updatedWorkOrder) {

        WorkOrder existing = getWorkOrderById(workOrderId);

        if (updatedWorkOrder.getPlannedCompletionDate() != null) {
            existing.setPlannedCompletionDate(
                    updatedWorkOrder.getPlannedCompletionDate());
        }

        if (updatedWorkOrder.getStatus() != null &&
                !updatedWorkOrder.getStatus().isBlank()) {
            existing.setStatus(updatedWorkOrder.getStatus());
        }

        if (existing.getPlannedCompletionDate()
                .isBefore(existing.getCreatedDate())) {
            throw new IllegalArgumentException(
                    "Planned completion date cannot be before created date");
        }

        return workOrderRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteWorkOrder(Long workOrderId) {

        WorkOrder existing = getWorkOrderById(workOrderId);

        workOrderRepository.delete(existing);
    }
}
