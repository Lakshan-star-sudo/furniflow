
package com.furniflow.service;

import com.furniflow.entity.OrderProduct;
import com.furniflow.entity.WorkOrder;
import com.furniflow.entity.WorkOrderItem;
import com.furniflow.exception.ResourceNotFoundException;
import com.furniflow.repository.OrderProductRepository;
import com.furniflow.repository.WorkOrderRepository;
import com.furniflow.repository.WorkOrderItemRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkOrderItemServiceImpl implements WorkOrderItemService {

    private final WorkOrderItemRepository itemRepository;
    private final WorkOrderRepository workOrderRepository;
    private final OrderProductRepository orderProductRepository;

    public WorkOrderItemServiceImpl(
            WorkOrderItemRepository itemRepository,
            WorkOrderRepository workOrderRepository,
            OrderProductRepository orderProductRepository) {

        this.itemRepository = itemRepository;
        this.workOrderRepository = workOrderRepository;
        this.orderProductRepository = orderProductRepository;
    }

    private WorkOrderItem findItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Work order item not found with ID: " + id));
    }

    private void validateQuantity(
            OrderProduct orderProduct,
            Integer quantity,
            Long excludedItemId) {

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }

        int allocated = itemRepository
                .findByOrderProduct_OrderProductId(
                        orderProduct.getOrderProductId())
                .stream()
                .filter(item -> !item.getWorkOrderItemId()
                        .equals(excludedItemId))
                .mapToInt(WorkOrderItem::getQuantity)
                .sum();

        if (allocated + (long) quantity > orderProduct.getQuantity()) {
            throw new IllegalArgumentException(
                    "Work order quantity exceeds ordered quantity");
        }
    }

    @Override
    @Transactional
    public WorkOrderItem createWorkOrderItem(
            Long workOrderId,
            Long orderProductId,
            Integer quantity) {

        WorkOrder workOrder = workOrderRepository
                .findById(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Work order not found with ID: " + workOrderId));

        OrderProduct orderProduct = orderProductRepository
                .findByIdForUpdate(orderProductId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order product not found with ID: " + orderProductId));

        if (!workOrder.getOrder().getOrderId()
                .equals(orderProduct.getOrder().getOrderId())) {
            throw new IllegalArgumentException(
                    "Order product does not belong to the work order's order");
        }

        validateQuantity(orderProduct, quantity, null);

        WorkOrderItem item = new WorkOrderItem();
        item.setWorkOrder(workOrder);
        item.setOrderProduct(orderProduct);
        item.setQuantity(quantity);
        item.setStatus("PENDING");

        return itemRepository.save(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkOrderItem> getAllWorkOrderItems() {
        return itemRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public WorkOrderItem getWorkOrderItemById(Long workOrderItemId) {
        return findItem(workOrderItemId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkOrderItem> getItemsByWorkOrder(Long workOrderId) {

        if (!workOrderRepository.existsById(workOrderId)) {
            throw new ResourceNotFoundException(
                    "Work order not found with ID: " + workOrderId);
        }

        return itemRepository
                .findByWorkOrder_WorkOrderId(workOrderId);
    }

    @Override
    @Transactional
    public WorkOrderItem updateWorkOrderItem(
            Long workOrderItemId,
            Integer quantity,
            String status) {

        WorkOrderItem item = findItem(workOrderItemId);

        OrderProduct orderProduct = orderProductRepository
                .findByIdForUpdate(
                        item.getOrderProduct().getOrderProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order product not found"));

        if (quantity != null) {
            validateQuantity(
                    orderProduct, quantity, workOrderItemId);
            item.setQuantity(quantity);
        }

        if (status != null) {
            if (status.isBlank()) {
                throw new IllegalArgumentException(
                        "Status cannot be blank");
            }

            item.setStatus(status);
        }

        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public void deleteWorkOrderItem(Long workOrderItemId) {

        WorkOrderItem item = findItem(workOrderItemId);

        orderProductRepository.findByIdForUpdate(
                item.getOrderProduct().getOrderProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order product not found"));

        itemRepository.delete(item);
    }
}
