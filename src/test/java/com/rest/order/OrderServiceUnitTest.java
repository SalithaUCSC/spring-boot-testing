package com.rest.order;

import com.rest.order.exceptions.OrderNotFoundException;
import com.rest.order.models.Order;
import com.rest.order.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class OrderServiceUnitTest {

    @Mock
    private OrderService orderService;

    @BeforeEach
    public void setup() {

    }

    @Test
    public void testGetOrdersList() {
        Order order1 = new Order(8L, "ben", 80.0, 5);
        Order order2 = new Order(9L, "kevin", 70.0, 2);
        when(orderService.getOrders()).thenReturn(Arrays.asList(order1, order2));
        assertEquals(orderService.getOrders().size(), 2);
        assertEquals(orderService.getOrders().get(0).getBuyer(), "ben");
        assertEquals(orderService.getOrders().get(1).getBuyer(), "kevin");
        assertEquals(orderService.getOrders().get(0).getPrice(), 80.0);
        assertEquals(orderService.getOrders().get(1).getPrice(), 70.0);
        assertNotEquals(orderService.getOrders().get(0).getBuyer(), null);
        assertNotEquals(orderService.getOrders().get(1).getBuyer(), null);
    }

    @Test
    public void testGetOrderById() {
        Order order = new Order(7L, "george", 60.0, 6);
        when(orderService.getOrderById(7L)).thenReturn(order);
        assertEquals(orderService.getOrderById(7L).getBuyer(), "george");
        assertEquals(orderService.getOrderById(7L).getPrice(), 60.0);
        assertNotEquals(orderService.getOrderById(7L).getBuyer(), null);
    }


    @Test
    public void testGetInvalidOrderById() {
        when(orderService.getOrderById(17L)).thenThrow(new OrderNotFoundException("Order Not Found with ID"));
        Exception exception = assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrderById(17L);
        });
        assertTrue(exception.getMessage().contains("Order Not Found with ID"));
    }

    @Test
    public void testCreateOrder() {
        Order order = new Order(12L, "john", 90.0, 6);
        orderService.createOrder(order);
        verify(orderService, times(1)).createOrder(order);
        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderService).createOrder(orderArgumentCaptor.capture());
        Order orderCreated = orderArgumentCaptor.getValue();
        assertNotNull(orderCreated.getId());
        assertEquals("john", orderCreated.getBuyer());
    }

    @Test
    public void testDeleteOrder() {
        Order order = new Order(13L, "simen", 120.0, 10);
        orderService.deleteOrderById(order.getId());
        verify(orderService, times(1)).deleteOrderById(order.getId());
        ArgumentCaptor<Long> orderArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(orderService).deleteOrderById(orderArgumentCaptor.capture());
        Long orderIdDeleted = orderArgumentCaptor.getValue();
        assertNotNull(orderIdDeleted);
        assertEquals(13L, orderIdDeleted);
    }
}
