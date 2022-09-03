package com.rest.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.order.models.Order;
import com.rest.order.repositories.OrderRepository;
import com.rest.order.services.OrderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    private static HttpHeaders headers;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

//    @BeforeTestClass
//    public void setup() {
//        orderRepository.deleteAll();
//    }


    @Test
    @Sql(statements = "INSERT INTO orders(id, buyer, price, qty) VALUES (2, 'john', 24, 1)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM orders WHERE id='2'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testOrdersList() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<List<Order>> response = restTemplate.exchange(
                createURLWithPort(), HttpMethod.GET, entity, new ParameterizedTypeReference<List<Order>>(){});
        List<Order> orderList = response.getBody();
        assert orderList != null;
        assertEquals(response.getStatusCodeValue(), 200);
        assertEquals(orderList.size(), orderService.getOrders().size());
        assertEquals(orderList.size(), orderRepository.findAll().size());
    }

    @Test
    @Sql(statements = "INSERT INTO orders(id, buyer, price, qty) VALUES (4, 'sam', 50, 4)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM orders WHERE id='4'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testOrderById() throws JsonProcessingException {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<Order> response = restTemplate.exchange(
                (createURLWithPort() + "/4"), HttpMethod.GET, entity, Order.class);
        Order orderRes = response.getBody();
        String expected = "{\"id\":4,\"buyer\":\"sam\",\"price\":50.0,\"qty\":4}";
        assertEquals(response.getStatusCodeValue(), 200);
        assertEquals(expected, objectMapper.writeValueAsString(orderRes));
        assert orderRes != null;
        assertEquals(orderRes, orderService.getOrderById(4L));
        assertEquals(orderRes.getBuyer(), orderService.getOrderById(4L).getBuyer());
        assertEquals(orderRes, orderRepository.findById(4L).orElse(null));
    }

    @Test
    public void testCreateOrder() throws JsonProcessingException {
        Order order = new Order(3L, "peter", 30.0, 3);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(order), headers);
        ResponseEntity<Order> response = restTemplate.exchange(
                createURLWithPort(), HttpMethod.POST, entity, Order.class);
        assertEquals(response.getStatusCodeValue(), 201);
        Order orderRes = Objects.requireNonNull(response.getBody());
        assertEquals(orderRes.getBuyer(), "peter");
    }

    private String createURLWithPort() {
        return "http://localhost:" + port + "/api/orders";
    }

}
