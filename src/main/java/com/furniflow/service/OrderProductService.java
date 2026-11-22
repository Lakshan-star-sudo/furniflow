package com.furniflow.service;

import com.furniflow.entity.OrderProduct;

import java.util.List;

public interface OrderProductService {

    OrderProduct addOrderProduct(OrderProduct orderProduct);

    List<OrderProduct> getAllOrderProducts();

    List<OrderProduct> getOrderProductsByOrderId(Long orderId);

    OrderProduct getOrderProductById(Long id);

    OrderProduct updateOrderProduct(Long id, OrderProduct orderProduct);

    void deleteOrderProduct(Long id);
}