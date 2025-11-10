package com.gtech.Ecommerce.repositories;

import com.gtech.Ecommerce.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
