package com.rest.order.services;

import com.rest.order.models.Order;

import java.util.List;

public interface OrderService {
    List<Order> getOrders();
    Order getOrderById(Long id);
    Order createOrder(Order order);
}
