package com.gtech.Ecommerce.repositories;

import com.gtech.Ecommerce.entities.OrderItem;
import com.gtech.Ecommerce.entities.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {
}
