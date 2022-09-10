package com.rest.order.services;

import com.rest.order.exceptions.OrderNotFoundException;
import com.rest.order.models.Order;
import com.rest.order.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> throwException(String.valueOf(id)));
    }

    public boolean deleteOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            orderRepository.deleteById(id);
            return true;
        } else {
            throwException(String.valueOf(id));
            return false;
        }
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    private OrderNotFoundException throwException(String value) {
        throw new OrderNotFoundException("Order Not Found with ID: " + value);
    }
}
