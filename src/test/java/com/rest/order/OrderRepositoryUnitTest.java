package com.rest.order;

import com.rest.order.models.Order;
import com.rest.order.repositories.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OrderRepositoryUnitTest {

    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        orderRepository.save(new Order(100L, "jane", 200.0, 2));
        orderRepository.save(new Order(200L, "ben", 100.0, 5));
    }

    @AfterEach
    public void destroy() {
        orderRepository.deleteAll();
    }

    @Test
    public void testGetAllOrders() {
        List<Order> orderList = orderRepository.findAll();
        Assertions.assertThat(orderList.size()).isEqualTo(2);
        Assertions.assertThat(orderList.get(0).getId()).isNotNegative();
        Assertions.assertThat(orderList.get(0).getId()).isGreaterThan(0);
        Assertions.assertThat(orderList.get(0).getBuyer()).isEqualTo("jane");
    }

    @Test
    public void testGetInvalidOrder() {
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            orderRepository.findById(120L).get();
        });
        Assertions.assertThat(exception).isNotNull();
        Assertions.assertThat(exception.getClass()).isEqualTo(NoSuchElementException.class);
        Assertions.assertThat(exception.getMessage()).isEqualTo("No value present");
    }

    @Test
    public void testGetCreateOrder() {
        Order saved = new Order(300L, "tim", 50.0, 4);
        Order returned = orderRepository.save(saved);
        Assertions.assertThat(returned).isNotNull();
        Assertions.assertThat(returned.getBuyer()).isNotEmpty();
        Assertions.assertThat(returned.getId()).isGreaterThan(1);
        Assertions.assertThat(returned.getId()).isNotNegative();
        Assertions.assertThat(saved.getBuyer()).isEqualTo(returned.getBuyer());
    }

    @Test
    public void testDeleteOrder() {
        Order saved = new Order(400L, "ron", 60.0, 3);
        orderRepository.save(saved);
        orderRepository.delete(saved);
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            orderRepository.findById(400L).get();
        });
        Assertions.assertThat(exception).isNotNull();
        Assertions.assertThat(exception.getClass()).isEqualTo(NoSuchElementException.class);
        Assertions.assertThat(exception.getMessage()).isEqualTo("No value present");
    }

}
