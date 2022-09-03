package com.rest.order;

import com.rest.order.models.Order;
import com.rest.order.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceUnitTest {


    @MockBean
    private OrderService orderService;

    private Order order;

    @BeforeEach
    public void setup() {
        order = new Order(8L, "ben", 80.0, 5);
    }

    @Test
    public void testGetOrdersList() {
        when(orderService.getOrders()).thenReturn(Collections.singletonList(order));
        assertEquals(orderService.getOrders().size(), 1);
        assertEquals(orderService.getOrders().get(0).getBuyer(), "ben");
        assertEquals(orderService.getOrders().get(0).getPrice(), 80.0);
        assertNotEquals(orderService.getOrders().get(0).getBuyer(), null);
    }

    @Test
    public void testGetOrderById() {
        Order order = new Order(7L, "george", 60.0, 6);
        when(orderService.getOrderById(7L)).thenReturn(order);
        assertEquals(orderService.getOrderById(7L).getBuyer(), "george");
        assertEquals(orderService.getOrderById(7L).getPrice(), 60.0);
        assertNotEquals(orderService.getOrderById(7L).getBuyer(), null);
    }
}
