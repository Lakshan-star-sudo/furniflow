package com.furniflow.service;

import com.furniflow.entity.Customer;
import com.furniflow.entity.Order;
import com.furniflow.entity.Showroom;
import com.furniflow.exception.ResourceNotFoundException;
import com.furniflow.repository.CustomerRepository;
import com.furniflow.repository.OrderRepository;
import com.furniflow.repository.ShowroomRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ShowroomRepository showroomRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            ShowroomRepository showroomRepository) {

        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.showroomRepository = showroomRepository;
    }

    @Override
    public Order createOrder(Order order) {

        if (order.getCustomer() == null ||
                order.getCustomer().getCustomerId() == null) {
            throw new RuntimeException("Customer is required");
        }

        if (order.getShowroom() == null ||
                order.getShowroom().getShowroomId() == null) {
            throw new RuntimeException("Showroom is required");
        }

        Customer customer = customerRepository
                .findById(order.getCustomer().getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        Showroom showroom = showroomRepository
                .findById(order.getShowroom().getShowroomId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Showroom not found"));

        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDate.now());
        }

        if (order.getRequiredDate() == null) {
            throw new RuntimeException("Required date is required");
        }

        if (order.getRequiredDate().isBefore(order.getOrderDate())) {
            throw new RuntimeException(
                    "Required date cannot be before order date");
        }

        if (order.getOrderType() == null ||
                order.getOrderType().trim().isEmpty()) {
            throw new RuntimeException("Order type is required");
        }

        if (order.getTotalAmount() == null) {
            order.setTotalAmount(BigDecimal.ZERO);
        }

        if (order.getTotalAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException(
                    "Total amount cannot be negative");
        }

        if (order.getStatus() == null ||
                order.getStatus().trim().isEmpty()) {
            order.setStatus("PENDING");
        }

        order.setCustomer(customer);
        order.setShowroom(showroom);

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {

        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));
    }

    @Override
    public Order updateOrder(Long id, Order order) {

        Order existing = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        if (order.getOrderType() != null) {
            existing.setOrderType(order.getOrderType());
        }

        if (order.getRequiredDate() != null) {

            if (order.getRequiredDate()
                    .isBefore(existing.getOrderDate())) {

                throw new RuntimeException(
                        "Required date cannot be before order date");
            }

            existing.setRequiredDate(order.getRequiredDate());
        }

        if (order.getStatus() != null) {
            existing.setStatus(order.getStatus());
        }

        if (order.getCustomer() != null &&
                order.getCustomer().getCustomerId() != null) {

            Customer customer = customerRepository
                    .findById(order.getCustomer().getCustomerId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Customer not found"));

            existing.setCustomer(customer);
        }

        if (order.getShowroom() != null &&
                order.getShowroom().getShowroomId() != null) {

            Showroom showroom = showroomRepository
                    .findById(order.getShowroom().getShowroomId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Showroom not found"));

            existing.setShowroom(showroom);
        }

        return orderRepository.save(existing);
    }

    @Override
    public void deleteOrder(Long id) {

        Order existing = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        orderRepository.delete(existing);
    }
}