package com.furniflow.service;

import com.furniflow.entity.Order;
import com.furniflow.entity.OrderProduct;
import com.furniflow.entity.Product;
import com.furniflow.exception.ResourceNotFoundException;
import com.furniflow.repository.OrderProductRepository;
import com.furniflow.repository.OrderRepository;
import com.furniflow.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderProductServiceImpl implements OrderProductService {

    private final OrderProductRepository orderProductRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderProductServiceImpl(
            OrderProductRepository orderProductRepository,
            OrderRepository orderRepository,
            ProductRepository productRepository) {

        this.orderProductRepository = orderProductRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OrderProduct addOrderProduct(OrderProduct orderProduct) {

        if (orderProduct.getOrder() == null ||
                orderProduct.getOrder().getOrderId() == null) {
            throw new RuntimeException("Order is required");
        }

        if (orderProduct.getProduct() == null ||
                orderProduct.getProduct().getProductId() == null) {
            throw new RuntimeException("Product is required");
        }

        if (orderProduct.getQuantity() == null ||
                orderProduct.getQuantity() <= 0) {
            throw new RuntimeException(
                    "Quantity must be greater than zero");
        }

        Order order = orderRepository
                .findById(orderProduct.getOrder().getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        Product product = productRepository
                .findById(orderProduct.getProduct().getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        if (orderProduct.getUnitPrice() == null) {
            orderProduct.setUnitPrice(product.getStandardPrice());
        }

        if (orderProduct.getUnitPrice()
                .compareTo(BigDecimal.ZERO) < 0) {

            throw new RuntimeException(
                    "Unit price cannot be negative");
        }

        orderProduct.setOrder(order);
        orderProduct.setProduct(product);

        OrderProduct saved =
                orderProductRepository.save(orderProduct);

        updateOrderTotal(order);

        return saved;
    }

    @Override
    public List<OrderProduct> getAllOrderProducts() {
        return orderProductRepository.findAll();
    }

    @Override
    public List<OrderProduct> getOrderProductsByOrderId(
            Long orderId) {

        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Order not found");
        }

        return orderProductRepository
                .findByOrderOrderId(orderId);
    }

    @Override
    public OrderProduct getOrderProductById(Long id) {

        return orderProductRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order product not found"));
    }

    @Override
    public OrderProduct updateOrderProduct(
            Long id,
            OrderProduct orderProduct) {

        OrderProduct existing =
                orderProductRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order product not found"));

        if (orderProduct.getQuantity() != null) {

            if (orderProduct.getQuantity() <= 0) {
                throw new RuntimeException(
                        "Quantity must be greater than zero");
            }

            existing.setQuantity(orderProduct.getQuantity());
        }

        if (orderProduct.getUnitPrice() != null) {

            if (orderProduct.getUnitPrice()
                    .compareTo(BigDecimal.ZERO) < 0) {

                throw new RuntimeException(
                        "Unit price cannot be negative");
            }

            existing.setUnitPrice(orderProduct.getUnitPrice());
        }

        if (orderProduct.getCustomDetails() != null) {
            existing.setCustomDetails(
                    orderProduct.getCustomDetails());
        }

        OrderProduct saved =
                orderProductRepository.save(existing);

        updateOrderTotal(existing.getOrder());

        return saved;
    }

    @Override
    public void deleteOrderProduct(Long id) {

        OrderProduct existing =
                orderProductRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order product not found"));

        Order order = existing.getOrder();

        orderProductRepository.delete(existing);

        updateOrderTotal(order);
    }

    private void updateOrderTotal(Order order) {

        List<OrderProduct> items =
                orderProductRepository
                        .findByOrderOrderId(order.getOrderId());

        BigDecimal total = BigDecimal.ZERO;

        for (OrderProduct item : items) {

            BigDecimal lineTotal =
                    item.getUnitPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            item.getQuantity()));

            total = total.add(lineTotal);
        }

        order.setTotalAmount(total);

        orderRepository.save(order);
    }
}