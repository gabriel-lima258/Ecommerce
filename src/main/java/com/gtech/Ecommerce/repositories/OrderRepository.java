package com.gtech.Ecommerce.repositories;

import com.gtech.Ecommerce.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
