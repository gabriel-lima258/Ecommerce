package com.gtech.Ecommerce.repositories;

import com.gtech.Ecommerce.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // consulta N+1 ManyToMany para consultar o banco somente uma vez, assim reduzindo várias consultas desnecessárias
    @Query(value = """ 
           SELECT obj FROM Product obj
           JOIN FETCH obj.categories
           WHERE UPPER(obj.name)
           LIKE UPPER(CONCAT('%', :name, '%'))
           """,
            countQuery = "SELECT COUNT(obj) FROM Product obj JOIN obj.categories")
    Page<Product> searchByName(String name, Pageable pageable);
}
