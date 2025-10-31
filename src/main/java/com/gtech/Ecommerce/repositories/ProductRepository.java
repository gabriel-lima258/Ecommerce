package com.gtech.Ecommerce.repositories;

import com.gtech.Ecommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
