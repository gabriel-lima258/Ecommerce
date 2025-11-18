package com.gtech.Ecommerce.services;

import com.gtech.Ecommerce.dto.product.ProductDTO;
import com.gtech.Ecommerce.dto.product.ProductMinDTO;
import com.gtech.Ecommerce.entities.Product;
import com.gtech.Ecommerce.factories.ProductFactoryTest;
import com.gtech.Ecommerce.repositories.ProductRepository;
import com.gtech.Ecommerce.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository productRepository;

    private Long existingId;
    private Long noExistingId;
    private Product product;
    private ProductDTO productDTO;
    private List<Product> list;
    private PageImpl<Product> page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        noExistingId = 121L;
        product = ProductFactoryTest.createProduct();
        productDTO = ProductFactoryTest.createProductDTO();
        list = new ArrayList<>();
        list.add(product);
        page = new PageImpl<>(List.of(product)); // instancio uma page de produtos

        // findAll
        Mockito.when(productRepository.searchByName(anyString(), (Pageable) any())).thenReturn(page);
        // findById
        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.ofNullable(product));
        Mockito.when(productRepository.findById(noExistingId)).thenReturn(Optional.empty());
        // insert
        Mockito.when(productRepository.save(any())).thenReturn(product);
    }

    @Test
    public void findAllShouldReturnListOfProductDTO() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<ProductMinDTO> result = service.findAll("Iphone", pageable);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Iphone", result.iterator().next().getName());
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = service.findById(existingId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Mockito.verify(productRepository).findById(existingId);
    }

    @Test
    public void findByIdShouldReturnThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            ProductDTO result = service.findById(noExistingId);
            Assertions.assertNull(result);
            Mockito.verify(productRepository).findById(noExistingId);
        });
    }

    @Test
    public void insertShouldReturnNewProductDTO() {
        ProductDTO result = service.insert(productDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(product.getId(), result.getId());
    }
}
