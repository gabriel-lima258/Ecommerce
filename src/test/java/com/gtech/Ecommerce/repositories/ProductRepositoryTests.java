package com.gtech.Ecommerce.repositories;

import com.gtech.Ecommerce.entities.Product;
import com.gtech.Ecommerce.entities.User;
import com.gtech.Ecommerce.factories.ProductFactoryTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

        @Autowired
        private ProductRepository repository;

        private long existingId;
        private long noExistingId;
        private long countTotalProduct;
        private String name;

        @BeforeEach
        void setUp() throws Exception {
            existingId = 1L;
            noExistingId = 200L;
            countTotalProduct = 25L;
            name = "Notebook";
        }

        @Test
        public void findByIdShouldNotBeEmptyWhenIdExists() {
            Optional<Product> result = repository.findById(existingId);
            Assertions.assertTrue(result.isPresent());
        }

        @Test
        public void findByIdShouldBeEmptyWhenIdDoesNotExists() {
            Optional<Product> result = repository.findById(noExistingId);
            Assertions.assertTrue(result.isEmpty());
        }

        @Test
        public void findAllShouldReturnPagedProductsSearchByName() {
            PageRequest pageable = PageRequest.of(0, 10);
            Page<Product> result = repository.searchByName(name, pageable);
            Assertions.assertNotNull(result);
        }

        @Test
        public void saveShouldPersistAutoIncrementWhenIdIsNull() {
            Product product = ProductFactoryTest.createProduct();
            product.setId(null);
            repository.save(product);
            Assertions.assertNotNull(product.getId());
            Assertions.assertEquals(countTotalProduct + 1, product.getId());
        }

        @Test
        public void deleteShouldDeleteObjectWhenIdExists() {
            repository.deleteById(existingId);
            Optional<Product> result = repository.findById(existingId);
            Assertions.assertFalse(result.isPresent());
        }

}
