package com.gtech.Ecommerce.factories;
import com.gtech.Ecommerce.dto.order.OrderDTO;
import com.gtech.Ecommerce.entities.*;

import java.time.Instant;

public class OrderFactoryTest {
    public static Order createOrder(User user) {
        Order order = new Order(1L, Instant.now(), OrderStatus.WAITING_PAYMENT, user, new Payment());
        Product product = ProductFactoryTest.createProduct();
        OrderItem orderItem = new OrderItem(order, product, 2, 250.0);
        order.getItems().add(orderItem);
        return order;
    }
}
