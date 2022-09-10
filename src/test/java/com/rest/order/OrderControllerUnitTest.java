package com.rest.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.order.controllers.OrderController;
import com.rest.order.models.Order;
import com.rest.order.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
public class OrderControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private Order order;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        order = new Order(10L, "andrew", 40.0, 2);
    }

    @Test
    public void testGetOrdersList() throws Exception {
        when(orderService.getOrders()).thenReturn(Collections.singletonList(order));
        mockMvc.perform(get("/api/orders"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetOrderById() throws Exception {
        when(orderService.getOrderById(10L)).thenReturn(order);
        mockMvc.perform(get("/api/orders/10"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.buyer", is("andrew")))
            .andExpect(jsonPath("$.id", is(10)))
            .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testCreateOrder() throws Exception {
        when(orderService.createOrder(order)).thenReturn(order);
        mockMvc.perform(
            post("/api/orders")
                .content(objectMapper.writeValueAsString(order))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.buyer", is("andrew")))
            .andExpect(jsonPath("$.id", is(10)))
            .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testDeleteOrder() throws Exception {
        when(orderService.deleteOrderById(order.getId())).thenReturn(true);
        mockMvc.perform(delete("/api/orders/" + order.getId()))
            .andDo(print())
            .andExpect(status().isOk());
    }

}
