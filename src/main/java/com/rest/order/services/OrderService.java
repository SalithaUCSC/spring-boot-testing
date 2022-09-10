package com.rest.order.services;

import com.rest.order.exceptions.OrderNotFoundException;
import com.rest.order.models.Order;
import com.rest.order.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order Not Found with ID: " + id));
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }
}
