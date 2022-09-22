package com.rest.order;

import com.rest.order.exceptions.OrderNotFoundException;
import com.rest.order.models.Order;
import com.rest.order.repositories.OrderRepository;
import com.rest.order.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class OrderServiceUnitTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    @BeforeEach
    public void setup() {

    }

    @Test
    public void testGetOrdersList() {
        Order order1 = new Order(8L, "ben", 80.0, 5);
        Order order2 = new Order(9L, "kevin", 70.0, 2);
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));
        List<Order> orderList = orderService.getOrders();
        assertEquals(orderList.size(), 2);
        assertEquals(orderList.get(0).getBuyer(), "ben");
        assertEquals(orderList.get(1).getBuyer(), "kevin");
        assertEquals(orderList.get(0).getPrice(), 80.0);
        assertEquals(orderList.get(1).getPrice(), 70.0);
    }

    @Test
    public void testGetOrderById() {
        Order order = new Order(7L, "george", 60.0, 6);
        when(orderRepository.findById(7L)).thenReturn(Optional.of(order));
        Order orderById = orderService.getOrderById(7L);
        assertNotEquals(orderById, null);
        assertEquals(orderById.getBuyer(), "george");
        assertEquals(orderById.getPrice(), 60.0);
    }


    @Test
    public void testGetInvalidOrderById() {
        when(orderRepository.findById(17L)).thenThrow(new OrderNotFoundException("Order Not Found with ID"));
        Exception exception = assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrderById(17L);
        });
        assertTrue(exception.getMessage().contains("Order Not Found with ID"));
    }

    @Test
    public void testCreateOrder() {
        Order order = new Order(12L, "john", 90.0, 6);
        orderService.createOrder(order);
        verify(orderRepository, times(1)).save(order);
        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());
        Order orderCreated = orderArgumentCaptor.getValue();
        assertNotNull(orderCreated.getId());
        assertEquals("john", orderCreated.getBuyer());
    }

    @Test
    public void testDeleteOrder() {
        Order order = new Order(13L, "simen", 120.0, 10);
        when(orderRepository.findById(13L)).thenReturn(Optional.of(order));
        orderService.deleteOrderById(order.getId());
        verify(orderRepository, times(1)).deleteById(order.getId());
        ArgumentCaptor<Long> orderArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(orderRepository).deleteById(orderArgumentCaptor.capture());
        Long orderIdDeleted = orderArgumentCaptor.getValue();
        assertNotNull(orderIdDeleted);
        assertEquals(13L, orderIdDeleted);
    }
}
